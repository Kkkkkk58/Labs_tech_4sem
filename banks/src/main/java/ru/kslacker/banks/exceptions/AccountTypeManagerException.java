package ru.kslacker.banks.exceptions;

public class AccountTypeManagerException extends BanksDomainException {

	private AccountTypeManagerException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when the account type provided to manager to create is of invalid type
	 *
	 * @return exception with corresponding message
	 */
	public static AccountTypeManagerException invalidAccountType() {
		return new AccountTypeManagerException("Provided invalid account type");
	}
}
