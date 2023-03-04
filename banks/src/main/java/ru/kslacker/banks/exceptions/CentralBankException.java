package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class CentralBankException extends BanksDomainException {

	private CentralBankException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when the bank with given id is already registered in central bank
	 *
	 * @param bankId id of the bank
	 * @return exception with corresponding message
	 */
	public static CentralBankException bankAlreadyExists(UUID bankId) {
		return new CentralBankException("Bank " + bankId + " already exists");
	}
}
