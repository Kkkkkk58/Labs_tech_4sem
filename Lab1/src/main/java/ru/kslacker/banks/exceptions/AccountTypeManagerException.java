package ru.kslacker.banks.exceptions;

public class AccountTypeManagerException extends BanksDomainException {

	private AccountTypeManagerException(String message) {
		super(message);
	}

	public static AccountTypeManagerException invalidAccountType()
	{
		return new AccountTypeManagerException("Provided invalid account type");
	}
}
