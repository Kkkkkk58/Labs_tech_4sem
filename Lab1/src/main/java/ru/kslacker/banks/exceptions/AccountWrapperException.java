package ru.kslacker.banks.exceptions;

public class AccountWrapperException extends BanksDomainException {

	private AccountWrapperException(String message) {
		super(message);
	}

	public static AccountWrapperException invalidWrappedType()
	{
		return new AccountWrapperException("Wrapped type is invalid");
	}
}
