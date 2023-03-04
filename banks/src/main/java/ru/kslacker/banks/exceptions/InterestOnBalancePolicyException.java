package ru.kslacker.banks.exceptions;

public class InterestOnBalancePolicyException extends BanksDomainException {

	private InterestOnBalancePolicyException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when some layers of policy have the same required balance
	 *
	 * @return exception with corresponding message
	 */
	public static InterestOnBalancePolicyException layersWithIntersectionsByInitialBalance() {
		return new InterestOnBalancePolicyException("Some layers have the same required balance");
	}
}
