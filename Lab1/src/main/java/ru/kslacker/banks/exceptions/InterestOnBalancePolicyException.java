package ru.kslacker.banks.exceptions;

public class InterestOnBalancePolicyException extends BanksDomainException {

	private InterestOnBalancePolicyException(String message) {
		super(message);
	}

	public static InterestOnBalancePolicyException layersWithIntersectionsByInitialBalance()
	{
		return new InterestOnBalancePolicyException("Some layers have the same required balance");
	}
}
