package ru.kslacker.banks.exceptions;

public class OperationInformationException extends BanksDomainException {

	private OperationInformationException(String message) {
		super(message);
	}

	public static OperationInformationException isAlreadyCompleted()
	{
		return new OperationInformationException("Operation is already completed");
	}
}
