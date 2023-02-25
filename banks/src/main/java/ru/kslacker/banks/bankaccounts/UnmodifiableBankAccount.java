package ru.kslacker.banks.bankaccounts;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;

public interface UnmodifiableBankAccount {

	/**
	 * Method to get id of bank account
	 *
	 * @return id of account
	 */
	UUID getId();

	/**
	 * Method to get type of account
	 *
	 * @return type of account
	 */
	AccountType getType();

	/**
	 * Method to get bank client being holder of the account
	 *
	 * @return holder of the account
	 */
	Customer getHolder();

	/**
	 * Method to get balance on account
	 *
	 * @return money amount kept on account
	 */
	MoneyAmount getBalance();

	/**
	 * Method to get value of debt
	 *
	 * @return debt value
	 */
	MoneyAmount getDebt();

	/**
	 * Method to get the date of creation of the account
	 *
	 * @return account creation date
	 */
	LocalDateTime getCreationDate();

	/**
	 * Method to get initial balance kept on account
	 *
	 * @return initial balance on account
	 */
	MoneyAmount getInitialBalance();

	/**
	 * Method to check whether the account is suspicious
	 *
	 * @return true if the account is suspicious, otherwise - false
	 */
	boolean isSuspicious();

	/**
	 * Method to get history of operations
	 *
	 * @return collection of operations info
	 */
	Collection<ReadOnlyOperationInformation> getOperationHistory();
}
