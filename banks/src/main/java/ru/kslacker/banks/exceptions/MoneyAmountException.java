package ru.kslacker.banks.exceptions;

import java.math.BigDecimal;
import java.util.Currency;

public class MoneyAmountException extends BanksDomainException {

	private MoneyAmountException(String message) {
		super(message);
	}

	public static MoneyAmountException negativeAmountException(BigDecimal value)
	{
		return new MoneyAmountException("Can't convert negative value " + value + " to money amount");
	}

	public static MoneyAmountException differentCurrenciesException(Currency a, Currency b)
	{
		return new MoneyAmountException(
			"Can't perform operations with different currencies: " + a + " and " + b);
	}
}
