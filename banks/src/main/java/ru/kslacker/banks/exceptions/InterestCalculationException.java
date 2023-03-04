package ru.kslacker.banks.exceptions;

public class InterestCalculationException extends BanksDomainException {

	private InterestCalculationException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when update date is invalid (ex. before current date)
	 *
	 * @return exception with corresponding message
	 */
	public static InterestCalculationException invalidUpdateDate() {
		return new InterestCalculationException("Update date is invalid");
	}
}
