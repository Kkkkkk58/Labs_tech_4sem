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

	protected Command() {
		id = UUID.randomUUID();
	}

	/**
	 * Method to execute command
	 *
	 * @param bankAccount account to execute command with
	 * @param transaction operation details
	 */
	public abstract void execute(BankAccount bankAccount, Transaction transaction);

	/**
	 * Method to cancel command
	 *
	 * @param transaction operation details
	 */
	public abstract void undo(Transaction transaction);
}
