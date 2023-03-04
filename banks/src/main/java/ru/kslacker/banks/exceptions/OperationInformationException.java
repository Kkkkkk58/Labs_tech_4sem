package ru.kslacker.banks.exceptions;

public class OperationInformationException extends BanksDomainException {

	private OperationInformationException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when trying to set completed status after completion
	 *
	 * @return exception with corresponding message
	 */
	public static OperationInformationException isAlreadyCompleted() {
		return new OperationInformationException("Operation is already completed");
	}
}
