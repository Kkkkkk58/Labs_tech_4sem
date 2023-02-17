package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class BankException extends BanksDomainException {

	private BankException(String message) {
		super(message);
	}

	public static BankException customerAlreadyExists(UUID bankId, UUID customerId)
	{
		return new BankException("Customer " + customerId +" already exists in bank " + bankId);
	}

	public static BankException customerNotFound(UUID customerId)
	{
		return new BankException("Customer " + customerId + " not found");
	}

	public static BankException accountNotFound(UUID accountId)
	{
		return new BankException("Account " + accountId + " not found");
	}

	public static BankException invalidAccountTypeCreation()
	{
		return new BankException("Tried to created an instance of invalid account type");
	}
}
