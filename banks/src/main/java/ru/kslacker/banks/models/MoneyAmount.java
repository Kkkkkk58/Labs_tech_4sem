package ru.kslacker.banks.models;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import ru.kslacker.banks.exceptions.MoneyAmountException;

public record MoneyAmount(BigDecimal value, Currency currency) implements Comparable<MoneyAmount> {

	/**
	 * Constructor of MoneyAmount with default currency
	 *
	 * @param value money amount
	 */
	public MoneyAmount(BigDecimal value) {
		this(value, Currency.getInstance(Locale.US));
	}

	/**
	 * Constructor of MoneyAmount
	 *
	 * @param value    money amount
	 * @param currency currency
	 */
	public MoneyAmount {
		if (value.compareTo(BigDecimal.ZERO) < 0) {
			throw MoneyAmountException.negativeAmountException(value);
		}
	}

	@Override
	public int compareTo(@NotNull MoneyAmount moneyAmount) {
		validateCurrencies(moneyAmount);
		return value.compareTo(moneyAmount.value);
	}

	/**
	 * Method to sum money amounts
	 *
	 * @param moneyAmount money amount to add
	 * @return sum of money amounts
	 */
	public MoneyAmount add(MoneyAmount moneyAmount) {
		validateCurrencies(moneyAmount);
		return new MoneyAmount(value.add(moneyAmount.value), currency);
	}

	/**
	 * Method to subtract money amounts
	 *
	 * @param moneyAmount money amount to subtract
	 * @return new money amount
	 */
	public MoneyAmount subtract(MoneyAmount moneyAmount) {
		validateCurrencies(moneyAmount);
		return new MoneyAmount(value.subtract(moneyAmount.value), currency);
	}

	/**
	 * Method to multiply money amount by decimal value
	 *
	 * @param decimalValue value to multiply by
	 * @return new money amount
	 */
	public MoneyAmount multipliedBy(BigDecimal decimalValue) {
		return new MoneyAmount(value.multiply(decimalValue), currency);
	}

	/**
	 * Method to identify whether MoneyAmount instances have mismatching currencies
	 *
	 * @param other other MoneyAmount
	 * @return true if instances have mismatching currencies
	 */
	public boolean hasDifferentCurrency(MoneyAmount other) {
		return !currency.equals(other.currency);
	}

	@Override
	public String toString() {
		return currency.toString() + " " + value.toString();
	}

	private void validateCurrencies(MoneyAmount other) {
		if (hasDifferentCurrency(other)) {
			throw MoneyAmountException.differentCurrenciesException(currency, other.currency);
		}
	}
}
