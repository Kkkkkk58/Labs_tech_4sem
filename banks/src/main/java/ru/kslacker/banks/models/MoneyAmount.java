package ru.kslacker.banks.models;

import org.jetbrains.annotations.NotNull;
import ru.kslacker.banks.exceptions.MoneyAmountException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public record MoneyAmount(BigDecimal value, Currency currency) implements Comparable<MoneyAmount> {

	public MoneyAmount(BigDecimal value) {
		this(value, Currency.getInstance(Locale.US));
	}

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

	public MoneyAmount add(MoneyAmount moneyAmount) {
		validateCurrencies(moneyAmount);
		return new MoneyAmount(value.add(moneyAmount.value), currency);
	}

	public MoneyAmount subtract(MoneyAmount moneyAmount) {
		validateCurrencies(moneyAmount);
		return new MoneyAmount(value.subtract(moneyAmount.value), currency);
	}

	public MoneyAmount multipliedBy(BigDecimal decimalValue) {
		return new MoneyAmount(value.multiply(decimalValue), currency);
	}

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
