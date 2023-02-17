package ru.kslacker.banks.exceptions;

public class InterestCalculationException extends BanksDomainException {

	private InterestCalculationException(String message) {
		super(message);
	}

	public static InterestCalculationException invalidUpdateDate()
	{
		return new InterestCalculationException("Update date is invalid");
	}
}
