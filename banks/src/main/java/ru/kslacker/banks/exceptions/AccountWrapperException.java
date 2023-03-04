package ru.kslacker.banks.exceptions;

public class AccountWrapperException extends BanksDomainException {

	private AccountWrapperException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when wrapped type doesn't match with wrapper
	 *
	 * @return exception with corresponding message
	 */
	public static AccountWrapperException invalidWrappedType() {
		return new AccountWrapperException("Wrapped type is invalid");
	}
}
