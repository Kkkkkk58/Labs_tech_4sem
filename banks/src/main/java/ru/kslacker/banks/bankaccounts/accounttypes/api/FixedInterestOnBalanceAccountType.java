package ru.kslacker.banks.bankaccounts.accounttypes.api;

import java.math.BigDecimal;

public interface FixedInterestOnBalanceAccountType extends InterestCalculationAccountType {

	/**
	 * Method to set interest on balance percent
	 *
	 * @param interestPercent new interest on balance percent
	 */
	void setInterestPercent(BigDecimal interestPercent);
}
