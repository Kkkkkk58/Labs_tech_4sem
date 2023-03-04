package ru.kslacker.banks.accounttypemanager.api;


import java.time.Period;
import java.util.UUID;

public interface InterestCalculatingTypeProvider {

	/**
	 * Method to change period of interest calculation of account type with given id
	 *
	 * @param typeId                    id of account type
	 * @param interestCalculationPeriod new period of interest calculation
	 */
	void setInterestCalculationPeriod(UUID typeId, Period interestCalculationPeriod);
}
