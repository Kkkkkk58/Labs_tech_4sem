package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class CustomerException extends BanksDomainException {

	private CustomerException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when trying to add notifier when initial notifier is not set
	 *
	 * @param customerId id of the customer
	 * @return exception with corresponding message
	 */
	public static CustomerException notifierIsNotSet(UUID customerId) {
		return new CustomerException("Customer " + customerId + " has no notifier to add");
	}
}
