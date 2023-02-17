package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class CustomerException extends BanksDomainException {

	private CustomerException(String message) {
		super(message);
	}

	public static CustomerException notifierIsNotSet(UUID customerId)
	{
		return new CustomerException("Customer " + customerId + " has no notifier to add");
	}
}
