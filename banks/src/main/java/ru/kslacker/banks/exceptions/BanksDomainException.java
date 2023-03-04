package ru.kslacker.banks.exceptions;

public class BanksDomainException extends RuntimeException {

	/**
	 * Constructor of domain exception
	 *
	 * @param message message of exception
	 */
	public BanksDomainException(String message) {
		super(message);
	}
}
