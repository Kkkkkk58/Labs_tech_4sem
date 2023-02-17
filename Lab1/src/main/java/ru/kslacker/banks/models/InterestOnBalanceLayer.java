package ru.kslacker.banks.models;

import java.math.BigDecimal;

public record InterestOnBalanceLayer(MoneyAmount requiredInitialBalance, BigDecimal interestOnBalance) {

	public InterestOnBalanceLayer {
		if (interestOnBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}
	}
}
