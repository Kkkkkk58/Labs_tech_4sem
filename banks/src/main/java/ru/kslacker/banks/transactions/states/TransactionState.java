package ru.kslacker.banks.transactions.states;

import ru.kslacker.banks.commands.Command;

public interface TransactionState {

	/**
	 * Method to execute command
	 *
	 * @param command command to execute
	 */
	void perform(Command command);

	/**
	 * Method to cancel operation
	 *
	 * @param command undo command
	 */
	void cancel(Command command);
}
