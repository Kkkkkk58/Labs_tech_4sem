package ru.kslacker.banks.console.extensions;

import lombok.experimental.UtilityClass;
import ru.kslacker.banks.models.MoneyAmount;
import java.math.BigDecimal;
import java.util.Currency;

@UtilityClass
public class StringExtensions {

	public static MoneyAmount toMoneyAmount(String s) {
		String[] parts = s.split(" ", 2);
		Currency currency = Currency.getInstance(parts[0]);
		BigDecimal value = new BigDecimal(parts[1]);

		return new MoneyAmount(value, currency);
	}
}
