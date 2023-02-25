package ru.kslacker.banks.transactions;

import ru.kslacker.banks.models.OperationInformation;
import ru.kslacker.banks.transactions.states.TransactionState;

public interface Transaction {

	/**
	 * Method to get information about operation
	 *
	 * @return information about operation
	 */
	OperationInformation getInformation();

	/**
	 * Method to set transaction state
	 *
	 * @param state transaction state
	 */
	void setState(TransactionState state);

	/**
	 * Method to execute transaction
	 */
	void perform();

	/**
	 * Method to cancel transaction
	 */
	void cancel();
}
