package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class CentralBankException extends BanksDomainException {

	private CentralBankException(String message) {
		super(message);
	}

	public static CentralBankException bankAlreadyExists(UUID bankId)
	{
		return new CentralBankException("Bank " + bankId + " already exists");
	}
}
