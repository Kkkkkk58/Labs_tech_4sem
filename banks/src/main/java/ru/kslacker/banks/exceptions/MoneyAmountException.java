package ru.kslacker.banks.exceptions;

import java.math.BigDecimal;
import java.util.Currency;

public class MoneyAmountException extends BanksDomainException {

	private MoneyAmountException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when trying to create negative money amount
	 *
	 * @param value transferred value
	 * @return exception with corresponding message
	 */
	public static MoneyAmountException negativeAmountException(BigDecimal value) {
		return new MoneyAmountException(
			"Can't convert negative value " + value + " to money amount");
	}

	/**
	 * Exception thrown when operation is performed with different currencies
	 *
	 * @param a MoneyAmount
	 * @param b another MoneyAmount
	 * @return exception with corresponding message
	 */
	public static MoneyAmountException differentCurrenciesException(Currency a, Currency b) {
		return new MoneyAmountException(
			"Can't perform operations with different currencies: " + a + " and " + b);
	}
}
