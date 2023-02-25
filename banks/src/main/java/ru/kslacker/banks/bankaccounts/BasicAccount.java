package ru.kslacker.banks.bankaccounts;

import lombok.Getter;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import ru.kslacker.banks.transactions.Transaction;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class BasicAccount implements BankAccount {

	private final List<ReadOnlyOperationInformation> operations;
	private final UUID id;
	private final AccountType type;
	private final Customer holder;
	private MoneyAmount balance;
	private MoneyAmount debt;
	private final LocalDateTime creationDate;
	private final MoneyAmount initialBalance;

	/**
	 * Constructor of basic account with withdrawal and replenishment
	 * @param type bank account type
	 * @param holder bank client, holder of the account
	 * @param balance initial balance of account
	 * @param clock instance of clock to track current time
	 */
	public BasicAccount(
		AccountType type,
		Customer holder,
		MoneyAmount balance,
		Clock clock) {

		this.id = UUID.randomUUID();
		this.operations = new ArrayList<>();
		this.type = type;
		this.holder = holder;
		this.initialBalance = this.balance = balance;
		this.debt = new MoneyAmount(BigDecimal.ZERO, balance.currency());
		this.creationDate = LocalDateTime.now(clock);
	}

	@Override
	public void execute(Command command, Transaction transaction) {
		command.execute(this, transaction);
	}

	@Override
	public boolean isSuspicious() {
		return !holder.isVerified();
	}

	@Override
	public Collection<ReadOnlyOperationInformation> getOperationHistory() {
		return Collections.unmodifiableList(operations);
	}

	@Override
	public MoneyAmount withdraw(Transaction transaction) {

		MoneyAmount moneyAmount = transaction.getInformation().getOperatedAmount();

		if (balance.compareTo(moneyAmount) < 0)
		{
			debt = debt.add(moneyAmount.subtract(balance));
			balance = new MoneyAmount(BigDecimal.ZERO, balance.currency());
		}
		else
		{
			balance = balance.subtract(moneyAmount);
		}

		operations.add(transaction.getInformation());
		return moneyAmount;
	}

	@Override
	public void replenish(Transaction transaction) {
		MoneyAmount moneyAmount = transaction.getInformation().getOperatedAmount();

		if (Objects.equals(debt.value(), BigDecimal.ZERO))
		{
			balance = balance.add(moneyAmount);
		}
		else
		{
			calculateNewDebt(moneyAmount);
		}

		operations.add(transaction.getInformation());
	}

	private void calculateNewDebt(MoneyAmount moneyAmount) {
		if (debt.compareTo(moneyAmount) <= 0)
		{
			balance = balance.add(moneyAmount.subtract(debt));
			debt = new MoneyAmount(BigDecimal.ZERO, debt.currency());
		}
		else
		{
			debt = debt.subtract(moneyAmount);
		}
	}
}
