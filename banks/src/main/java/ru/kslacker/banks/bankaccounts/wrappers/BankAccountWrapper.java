package ru.kslacker.banks.bankaccounts.wrappers;

import lombok.AllArgsConstructor;
import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.exceptions.TransactionException;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import ru.kslacker.banks.transactions.Transaction;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
public abstract class BankAccountWrapper implements BankAccount {

	private final BankAccount wrapped;

	@Override
	public MoneyAmount withdraw(Transaction transaction) {
		return wrapped.withdraw(transaction);
	}

	@Override
	public void replenish(Transaction transaction) {
		wrapped.replenish(transaction);
	}

	@Override
	public void execute(Command command, Transaction transaction) {
		try {
			command.execute(this, transaction);
		} catch (TransactionException e) {
			transaction.cancel();
			throw e;
		}
	}

	@Override
	public UUID getId() {
		return wrapped.getId();
	}

	@Override
	public AccountType getType() {
		return wrapped.getType();
	}

	@Override
	public Customer getHolder() {
		return wrapped.getHolder();
	}

	@Override
	public MoneyAmount getBalance() {
		return wrapped.getBalance();
	}

	@Override
	public MoneyAmount getDebt() {
		return wrapped.getDebt();
	}

	@Override
	public LocalDateTime getCreationDate() {
		return wrapped.getCreationDate();
	}

	@Override
	public MoneyAmount getInitialBalance() {
		return wrapped.getInitialBalance();
	}

	@Override
	public boolean isSuspicious() {
		return wrapped.isSuspicious();
	}

	@Override
	public Collection<ReadOnlyOperationInformation> getOperationHistory() {
		return wrapped.getOperationHistory();
	}
}
