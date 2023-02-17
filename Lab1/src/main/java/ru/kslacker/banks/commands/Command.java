package ru.kslacker.banks.commands;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.transactions.Transaction;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public abstract class Command {

	private final UUID id;

	protected Command()
	{
		id = UUID.randomUUID();
	}

	public abstract void execute(BankAccount bankAccount, Transaction transaction);
	public abstract void undo(Transaction transaction);
}
