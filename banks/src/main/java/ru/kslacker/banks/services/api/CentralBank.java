package ru.kslacker.banks.services.api;

import java.util.Collection;
import java.util.UUID;
import ru.kslacker.banks.entities.api.Bank;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;

public interface CentralBank {

	/**
	 * Method to get the list of registered banks
	 *
	 * @return collection of information about banks
	 */
	Collection<NoTransactionalBank> getBanks();

	/**
	 * Method to get the list of registered transactions
	 *
	 * @return collection of information about operations
	 */
	Collection<ReadOnlyOperationInformation> getOperations();

	/**
	 * Method to register new bank
	 *
	 * @param bank bank to register
	 * @return information about registered bank
	 */
	NoTransactionalBank registerBank(Bank bank);

	/**
	 * Method to cancel transaction by id
	 *
	 * @param transactionId id of transaction to cancel
	 */
	void cancelTransaction(UUID transactionId);

	/**
	 * Method to perform withdrawal of money
	 *
	 * @param accountId   account id to withdraw from
	 * @param moneyAmount withdrawn amount
	 * @return information about completed transaction
	 */
	ReadOnlyOperationInformation withdraw(UUID accountId, MoneyAmount moneyAmount);

	/**
	 * Method to perform replenishment of money
	 *
	 * @param accountId   account id to replenish
	 * @param moneyAmount replenishment amount
	 * @return information about completed transaction
	 */
	ReadOnlyOperationInformation replenish(UUID accountId, MoneyAmount moneyAmount);

	/**
	 * Method to perform transfer of money
	 *
	 * @param fromAccountId account id to transfer from
	 * @param toAccountId   account id to transfer to
	 * @param moneyAmount   transfer amount
	 * @return information about completed transaction
	 */
	ReadOnlyOperationInformation transfer(
		UUID fromAccountId,
		UUID toAccountId,
		MoneyAmount moneyAmount);
}
