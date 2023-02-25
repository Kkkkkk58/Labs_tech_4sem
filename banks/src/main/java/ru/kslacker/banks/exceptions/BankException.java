package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class BankException extends BanksDomainException {

	private BankException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when customer is already registered in bank
	 *
	 * @param bankId     id of the bank
	 * @param customerId id of the customer
	 * @return exception with corresponding message
	 */
	public static BankException customerAlreadyExists(UUID bankId, UUID customerId) {
		return new BankException("Customer " + customerId + " already exists in bank " + bankId);
	}

	/**
	 * Exception thrown when customer wasn't found among registered
	 *
	 * @param customerId id of the customer
	 * @return exception with corresponding message
	 */
	public static BankException customerNotFound(UUID customerId) {
		return new BankException("Customer " + customerId + " not found");
	}

	/**
	 * Exception thrown when account with given id is not found
	 *
	 * @param accountId id of the account
	 * @return exception with corresponding message
	 */
	public static BankException accountNotFound(UUID accountId) {
		return new BankException("Account " + accountId + " not found");
	}

	/**
	 * Exception thrown when invalid account type is provided for creation
	 *
	 * @return exception with corresponding message
	 */
	public static BankException invalidAccountTypeCreation() {
		return new BankException("Tried to created an instance of invalid account type");
	}
}
