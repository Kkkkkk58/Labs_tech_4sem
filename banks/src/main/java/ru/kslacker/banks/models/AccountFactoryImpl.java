package ru.kslacker.banks.models;

import java.time.Clock;
import lombok.AllArgsConstructor;
import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.BasicAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.bankaccounts.wrappers.ChargeableAfterDebtBankAccount;
import ru.kslacker.banks.bankaccounts.wrappers.DebtLimitedBankAccount;
import ru.kslacker.banks.bankaccounts.wrappers.InterestCalculationBankAccount;
import ru.kslacker.banks.bankaccounts.wrappers.NonNegativeBalanceBankAccount;
import ru.kslacker.banks.bankaccounts.wrappers.SuspiciousLimitingBankAccount;
import ru.kslacker.banks.bankaccounts.wrappers.TimeLimitedWithdrawalBankAccount;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.eventargs.DateChangedEventArgs;
import ru.kslacker.banks.tools.eventhandling.Subscribable;

@AllArgsConstructor
public class AccountFactoryImpl implements AccountFactory {

	private final Clock clock;
	private final Subscribable<DateChangedEventArgs> dateChanger;

	@Override
	public BankAccount createDebitAccount(DebitAccountType type, Customer customer,
		MoneyAmount balance) {

		BankAccount debitAccount = new BasicAccount(type, customer, balance, clock);
		debitAccount = new NonNegativeBalanceBankAccount(debitAccount);
		debitAccount = new InterestCalculationBankAccount(debitAccount, dateChanger);
		debitAccount = new SuspiciousLimitingBankAccount(debitAccount);

		return debitAccount;
	}

	@Override
	public BankAccount createDepositAccount(DepositAccountType type, Customer customer,
		MoneyAmount balance) {

		BankAccount depositAccount = new BasicAccount(type, customer, balance, clock);
		depositAccount = new NonNegativeBalanceBankAccount(depositAccount);
		depositAccount = new InterestCalculationBankAccount(depositAccount, dateChanger);
		depositAccount = new TimeLimitedWithdrawalBankAccount(depositAccount, clock);
		depositAccount = new SuspiciousLimitingBankAccount(depositAccount);

		return depositAccount;
	}

	@Override
	public BankAccount createCreditAccount(CreditAccountType type, Customer customer,
		MoneyAmount balance) {

		BankAccount creditAccount = new BasicAccount(type, customer, balance, clock);
		creditAccount = new DebtLimitedBankAccount(creditAccount);
		creditAccount = new ChargeableAfterDebtBankAccount(creditAccount);
		creditAccount = new SuspiciousLimitingBankAccount(creditAccount);

		return creditAccount;
	}
}
