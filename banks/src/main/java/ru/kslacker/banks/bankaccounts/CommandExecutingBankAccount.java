package ru.kslacker.banks.bankaccounts;

import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.transactions.Transaction;

public interface CommandExecutingBankAccount extends UnmodifiableBankAccount {

	/**
	 * Method to execute command
	 *
	 * @param command     command
	 * @param transaction information about transaction to perform
	 */
	void execute(Command command, Transaction transaction);
}
