package ru.kslacker.banks.bankaccounts.accounttypes.api;

import ru.kslacker.banks.models.MoneyAmount;

import java.math.BigDecimal;
import java.time.Period;

public interface InterestCalculationAccountType extends AccountType {

	/**
	 * Method to get period of interest calculation
	 *
	 * @return period of interest calculation
	 */
	Period getInterestCalculationPeriod();

	/**
	 * Method to set interest on balance percent
	 *
	 * @param interestCalculationPeriod new period of interest calculation
	 */
	void setInterestCalculationPeriod(Period interestCalculationPeriod);

	/**
	 * Method to get interest on balance based on initial balance of account
	 *
	 * @param initialBalance initial balance of account
	 * @return corresponding interest on balance value
	 */
	BigDecimal getInterestPercent(MoneyAmount initialBalance);
}
