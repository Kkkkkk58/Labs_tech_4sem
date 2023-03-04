package ru.kslacker.banks.bankaccounts.wrappers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.InterestCalculationAccountType;
import ru.kslacker.banks.commands.ReplenishmentCommand;
import ru.kslacker.banks.eventargs.DateChangedEventArgs;
import ru.kslacker.banks.exceptions.AccountWrapperException;
import ru.kslacker.banks.exceptions.InterestCalculationException;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.OperationInformation;
import ru.kslacker.banks.tools.eventhandling.Subscribable;
import ru.kslacker.banks.transactions.Transaction;
import ru.kslacker.banks.transactions.TransactionImpl;
import ru.kslacker.banks.transactions.states.SuccessfulTransactionState;

public class InterestCalculationBankAccount extends BankAccountWrapper {

	private final InterestCalculationAccountType type;
	private MoneyAmount savings;
	private LocalDateTime lastUpdate;
	private LocalDateTime date;

	/**
	 * Constructor of bank account that performs interest on balance calculations
	 *
	 * @param wrapped wrapped bank account
	 * @param updater instance of updater to send notifications about interest calculation
	 */
	public InterestCalculationBankAccount(
		BankAccount wrapped,
		Subscribable<DateChangedEventArgs> updater) {

		super(wrapped);

		if (!(wrapped.getType() instanceof InterestCalculationAccountType accountType)) {
			throw AccountWrapperException.invalidWrappedType();
		}

		this.type = accountType;
		this.date = this.lastUpdate = getCreationDate();
		this.savings = getBalance().multipliedBy(getPercent(date));
		updater.subscribe(this::update);
	}

	public void replenish(Transaction transaction) {
		super.replenish(transaction);
		savings = savings.add(
			transaction.getInformation().getOperatedAmount().multipliedBy(getPercent(date)));
	}

	public MoneyAmount withdraw(Transaction transaction) {
		super.withdraw(transaction);

		MoneyAmount moneyAmount = transaction.getInformation().getOperatedAmount();
		savings = savings.subtract(moneyAmount.multipliedBy(getPercent(date)));

		return moneyAmount;
	}

	private void update(Object sender, DateChangedEventArgs eventArgs) {

		if (eventArgs.date().isBefore(date)) {
			throw InterestCalculationException.invalidUpdateDate();
		}

		date = eventArgs.date();
		int percentCalculationTimes = getInterestCalculationTimes();
		if (percentCalculationTimes == 0) {
			return;
		}

		OperationInformation operationInformation = new OperationInformation(this, savings, date);
		Transaction transaction = new TransactionImpl(
			operationInformation,
			new ReplenishmentCommand());
		addPercents(percentCalculationTimes, transaction);
		lastUpdate = lastUpdate.plus(
			type.getInterestCalculationPeriod().multipliedBy(percentCalculationTimes));
	}

	private BigDecimal getPercent(LocalDateTime date) {
		int days = Year.of(date.getYear()).length();
		return type.getInterestPercent(getInitialBalance())
			.divide(BigDecimal.valueOf(days), 10, RoundingMode.HALF_UP);
	}

	private void addPercents(int percentCalculationTimes, Transaction transaction) {
		for (int i = 1; i < percentCalculationTimes; ++i) {
			LocalDateTime date = lastUpdate.plusMonths(i);
			savings = savings.multipliedBy(getPercent(date).add(BigDecimal.ONE));
		}

		transaction.getInformation().setOperatedAmount(savings);
		transaction.perform();
		transaction.getInformation().setCompletionTime(date);
		transaction.setState(new SuccessfulTransactionState(transaction));
	}

	private int getInterestCalculationTimes() {
		return (int) (ChronoUnit.DAYS.between(lastUpdate, date)
			/ type.getInterestCalculationPeriod().get(ChronoUnit.DAYS));
	}
}
