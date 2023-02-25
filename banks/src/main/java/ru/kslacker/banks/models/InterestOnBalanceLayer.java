package ru.kslacker.banks.models;

import java.math.BigDecimal;

public record InterestOnBalanceLayer(MoneyAmount requiredInitialBalance,
									 BigDecimal interestOnBalance) {

	/**
	 * Constructor of interest on balance layer
	 *
	 * @param requiredInitialBalance initial balance
	 * @param interestOnBalance      corresponding interest on balance
	 */
	public InterestOnBalanceLayer {
		if (interestOnBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}
	}
}
