package ru.kslacker.banks.exceptions;

public class BanksDomainException extends RuntimeException {

	public BanksDomainException(String message) {
		super(message);
	}
}
