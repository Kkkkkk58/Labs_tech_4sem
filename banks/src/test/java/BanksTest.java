import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.entities.BankImpl;
import ru.kslacker.banks.entities.CustomerImpl;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.exceptions.TransactionException;
import ru.kslacker.banks.exceptions.TransactionStateException;
import ru.kslacker.banks.models.*;
import ru.kslacker.banks.services.CentralBankImpl;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import ru.kslacker.banks.tools.clock.FastForwardingSubscribableClock;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.List;


public class BanksTest {

	private CentralBank centralBank;
	private FastForwardingClock clock;
	private AccountFactory accountFactory;

	@BeforeEach
	public void setup() {
		FastForwardingSubscribableClock clock = new FastForwardingSubscribableClock(
			LocalDateTime.now(), ZoneId.systemDefault());
		this.clock = clock;
		this.accountFactory = new AccountFactoryImpl(clock, clock);
		this.centralBank = new CentralBankImpl(clock);
	}

	@Test
	public void registerClientWithAddressAndPassportData_ClientIsVerified() {
		Customer client = getVerifiedCustomer();
		Assertions.assertTrue(client.isVerified());
	}

	@Test
	public void registerClientWithoutAddressAndPassportData_ClientIsNotVerified() {
		Customer client = getNotVerifiedCustomer();
		Assertions.assertFalse(client.isVerified());
	}

	@Test
	public void replenishMoney_BalanceIncreases() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(10));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		DebitAccountType accountType = bank.getAccountTypeManager()
			.createDebitAccountType(BigDecimal.valueOf(12), Period.ofMonths(1));
		MoneyAmount initialBalance = new MoneyAmount(BigDecimal.valueOf(15));
		UnmodifiableBankAccount account = bank.createDebitAccount(accountType, client,
			initialBalance);

		MoneyAmount replenishment = new MoneyAmount(BigDecimal.valueOf(12));
		centralBank.replenish(account.getId(), replenishment);
		Assertions.assertEquals(initialBalance.add(replenishment), account.getBalance());
	}

	@Test
	public void withdrawMoney_BalanceDecreases() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(10));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		DebitAccountType accountType = bank.getAccountTypeManager()
			.createDebitAccountType(BigDecimal.valueOf(12), Period.ofMonths(1));
		MoneyAmount initialBalance = new MoneyAmount(BigDecimal.valueOf(15));
		UnmodifiableBankAccount account = bank.createDebitAccount(accountType, client,
			initialBalance);

		MoneyAmount withdrawal = new MoneyAmount(BigDecimal.valueOf(12));
		centralBank.withdraw(account.getId(), withdrawal);
		Assertions.assertEquals(initialBalance.subtract(withdrawal), account.getBalance());
	}

	@Test
	public void transferMoney_ReceiverGetsMoneySenderGivesMoney() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(10));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		DebitAccountType accountType = bank.getAccountTypeManager()
			.createDebitAccountType(BigDecimal.valueOf(12), Period.ofMonths(1));
		MoneyAmount initialBalance = new MoneyAmount(BigDecimal.valueOf(15));
		UnmodifiableBankAccount sender = bank.createDebitAccount(accountType, client,
			initialBalance);
		UnmodifiableBankAccount receiver = bank.createDebitAccount(accountType, client,
			initialBalance);

		MoneyAmount transfer = new MoneyAmount(BigDecimal.valueOf(12));
		centralBank.transfer(sender.getId(), receiver.getId(), transfer);
		Assertions.assertEquals(initialBalance.subtract(transfer), sender.getBalance());
		Assertions.assertEquals(initialBalance.add(transfer), receiver.getBalance());
	}

	@Test
	public void performOperationBeyondSuspiciousOperationLimit_ThrowsException() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(10));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getNotVerifiedCustomer();
		bank.registerCustomer(client);
		DebitAccountType accountType = bank.getAccountTypeManager()
			.createDebitAccountType(BigDecimal.valueOf(12), Period.ofMonths(1));
		MoneyAmount initialBalance = new MoneyAmount(BigDecimal.ZERO);
		UnmodifiableBankAccount account = bank.createDebitAccount(accountType, client,
			initialBalance);

		Assertions.assertThrows(TransactionException.class,
			() -> centralBank.replenish(account.getId(), new MoneyAmount(BigDecimal.valueOf(15))));
	}

	@Test
	public void reachDebtWithNonNegativeBalanceAccount_ThrowsException() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(10));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		DebitAccountType accountType = bank.getAccountTypeManager()
			.createDebitAccountType(BigDecimal.valueOf(12), Period.ofMonths(1));
		MoneyAmount initialBalance = new MoneyAmount(BigDecimal.ZERO);
		UnmodifiableBankAccount account = bank.createDebitAccount(accountType, client,
			initialBalance);

		Assertions.assertThrows(TransactionException.class,
			() -> centralBank.withdraw(account.getId(), new MoneyAmount(BigDecimal.ONE)));
	}

	@Test
	public void reachDebtWithNegativeBalanceAvailableBankAccount_HasDebt() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(100_000));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		CreditAccountType accountType = bank.getAccountTypeManager()
			.createCreditAccountType(new MoneyAmount(BigDecimal.valueOf(15)),
				new MoneyAmount(BigDecimal.ZERO));
		UnmodifiableBankAccount account = bank.createCreditAccount(accountType, client,
			new MoneyAmount(BigDecimal.ZERO));

		MoneyAmount withdrawalAmount = new MoneyAmount(BigDecimal.valueOf(10));
		centralBank.withdraw(account.getId(), withdrawalAmount);
		Assertions.assertEquals(account.getBalance(), new MoneyAmount(BigDecimal.ZERO));
		Assertions.assertEquals(account.getDebt(), withdrawalAmount);
	}

	@Test
	public void reachDebtLimitWithCreditAccount_ThrowsException() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(100_000));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		CreditAccountType accountType = bank.getAccountTypeManager()
			.createCreditAccountType(new MoneyAmount(BigDecimal.valueOf(15)),
				new MoneyAmount(BigDecimal.ZERO));
		UnmodifiableBankAccount account = bank.createCreditAccount(accountType, client,
			new MoneyAmount(BigDecimal.ZERO));

		Assertions.assertThrows(TransactionException.class,
			() -> centralBank.withdraw(account.getId(), new MoneyAmount(BigDecimal.valueOf(20))));
	}

	@Test
	public void creditAccountWithDebt_AccountCharged() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(100_000));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		MoneyAmount charge = new MoneyAmount(BigDecimal.valueOf(10));
		CreditAccountType accountType = bank.getAccountTypeManager()
			.createCreditAccountType(new MoneyAmount(BigDecimal.valueOf(10000)), charge);
		UnmodifiableBankAccount account = bank.createCreditAccount(accountType, client,
			new MoneyAmount(BigDecimal.ZERO));

		MoneyAmount withdrawal = new MoneyAmount(BigDecimal.ONE);
		centralBank.withdraw(account.getId(), withdrawal);
		centralBank.withdraw(account.getId(), withdrawal);
		Assertions.assertEquals((withdrawal.multipliedBy(BigDecimal.valueOf(2))).add(charge),
			account.getDebt());
	}

	@Test
	public void depositAccountWithdrawalBeforeLimit_ThrowsException() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(100000));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getNotVerifiedCustomer();
		bank.registerCustomer(client);
		Period depositTerm = Period.ofDays(90);
		InterestOnBalancePolicy interestOnBalancePolicy = getInterestOnBalancePolicy();
		DepositAccountType accountType = bank.getAccountTypeManager()
			.createDepositAccountType(depositTerm, interestOnBalancePolicy, Period.ofMonths(1));
		UnmodifiableBankAccount account = bank.createDepositAccount(accountType, client,
			new MoneyAmount(BigDecimal.ZERO));

		Assertions.assertThrows(TransactionException.class,
			() -> centralBank.withdraw(account.getId(), new MoneyAmount(BigDecimal.ONE)));
	}

	@Test
	public void depositAccountsWithDifferentBalances_AccountsHaveSuitableInterestOnBalance() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(100000));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getNotVerifiedCustomer();
		bank.registerCustomer(client);
		Period depositTerm = Period.ofDays(90);
		InterestOnBalancePolicy interestOnBalancePolicy = getInterestOnBalancePolicy();
		DepositAccountType accountType = bank.getAccountTypeManager()
			.createDepositAccountType(depositTerm, interestOnBalancePolicy, Period.ofMonths(1));

		Assertions.assertEquals(BigDecimal.valueOf(3),
			accountType.getInterestPercent(new MoneyAmount(BigDecimal.valueOf(49_999))));
		Assertions.assertEquals(BigDecimal.valueOf(3.5),
			accountType.getInterestPercent(new MoneyAmount(BigDecimal.valueOf(50_000))));
	}

	@Test
	public void accountWithInterestOnBalance_BalanceIncreasedAfterTimeSkip() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(10));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		Period interestCalculationPeriod = Period.ofDays(30);
		final BigDecimal interestOnBalance = BigDecimal.valueOf(3.65);
		DebitAccountType accountType = bank.getAccountTypeManager()
			.createDebitAccountType(interestOnBalance, interestCalculationPeriod);
		MoneyAmount initialBalance = new MoneyAmount(BigDecimal.valueOf(15));
		UnmodifiableBankAccount account = bank.createDebitAccount(accountType, client,
			initialBalance);

		clock.skipDays(30);
		MoneyAmount expected = getExpectedBalanceAfterInterestCalculation(initialBalance,
			interestOnBalance);
		Assertions.assertEquals(expected, account.getBalance());
	}

	@Test
	public void cancelTransaction_AccountBalancesRemainAsBefore() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(10));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		DebitAccountType accountType = bank.getAccountTypeManager()
			.createDebitAccountType(BigDecimal.valueOf(12), Period.ofMonths(1));
		MoneyAmount initialBalance = new MoneyAmount(BigDecimal.valueOf(15));
		UnmodifiableBankAccount sender = bank.createDebitAccount(accountType, client,
			initialBalance);
		UnmodifiableBankAccount receiver = bank.createDebitAccount(accountType, client,
			initialBalance);

		MoneyAmount transfer = new MoneyAmount(BigDecimal.valueOf(12));
		ReadOnlyOperationInformation operationInformation = centralBank.transfer(sender.getId(),
			receiver.getId(), transfer);
		Assertions.assertEquals(initialBalance.subtract(transfer), sender.getBalance());
		Assertions.assertEquals(initialBalance.add(transfer), receiver.getBalance());

		centralBank.cancelTransaction(operationInformation.getId());
		Assertions.assertEquals(initialBalance, sender.getBalance());
		Assertions.assertEquals(initialBalance, receiver.getBalance());
	}

	@Test
	public void cancelCancelledTransaction_ThrowsException() {
		MoneyAmount suspiciousOperationsLimit = new MoneyAmount(BigDecimal.valueOf(10));
		NoTransactionalBank bank = getBank(suspiciousOperationsLimit);
		Customer client = getVerifiedCustomer();
		bank.registerCustomer(client);
		DebitAccountType accountType = bank.getAccountTypeManager()
			.createDebitAccountType(BigDecimal.valueOf(12), Period.ofMonths(1));
		MoneyAmount initialBalance = new MoneyAmount(BigDecimal.valueOf(15));
		UnmodifiableBankAccount sender = bank.createDebitAccount(accountType, client,
			initialBalance);
		UnmodifiableBankAccount receiver = bank.createDebitAccount(accountType, client,
			initialBalance);

		var transfer = new MoneyAmount(BigDecimal.valueOf(12));
		ReadOnlyOperationInformation operationInformation = centralBank.transfer(sender.getId(),
			receiver.getId(), transfer);

		centralBank.cancelTransaction(operationInformation.getId());
		Assertions.assertThrows(TransactionStateException.class,
			() -> centralBank.cancelTransaction(operationInformation.getId()));
	}

	private Customer getVerifiedCustomer() {
		return CustomerImpl
			.getCustomerBuilder()
			.withFirstName("Kracker")
			.withLastName("Slacker")
			.withAddress(new Address("Vyazemskii per 5/7"))
			.withPassportData(new PassportData(LocalDate.now(), "228337"))
			.build();
	}

	private Customer getNotVerifiedCustomer() {
		return CustomerImpl
			.getCustomerBuilder()
			.withFirstName("Kracker")
			.withLastName("Slacker")
			.build();
	}

	private InterestOnBalancePolicy getInterestOnBalancePolicy() {
		return new InterestOnBalancePolicy(List.of(
			new InterestOnBalanceLayer(new MoneyAmount(BigDecimal.ZERO), BigDecimal.valueOf(3)),
			new InterestOnBalanceLayer(new MoneyAmount(BigDecimal.valueOf(50_000)),
				BigDecimal.valueOf(3.5)),
			new InterestOnBalanceLayer(new MoneyAmount(BigDecimal.valueOf(100_000)),
				BigDecimal.valueOf(4))
		));
	}

	private NoTransactionalBank getBank(MoneyAmount suspiciousOperationsLimit) {
		return centralBank.registerBank(
			new BankImpl("Krugloff", accountFactory, suspiciousOperationsLimit, clock));
	}

	private MoneyAmount getExpectedBalanceAfterInterestCalculation(MoneyAmount initialBalance,
		BigDecimal interestOnBalance) {
		return initialBalance
			.multipliedBy(BigDecimal.ONE.add(interestOnBalance.divide(
				BigDecimal.valueOf(Year.of(LocalDateTime.now(clock).getYear()).length()), 10,
				RoundingMode.HALF_UP)));
	}

}
