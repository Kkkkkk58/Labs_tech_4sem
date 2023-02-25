package ru.kslacker.banks.accounttypemanager.api;

import java.math.BigDecimal;
import java.time.Period;
import java.util.UUID;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;

public interface DebitTypeProvider extends
	SuspiciousLimitingTypeProvider,
	InterestCalculatingTypeProvider {

	/**
	 * Method to create new debit account type
	 *
	 * @param interestOnBalance         value of annual interest on balance
	 * @param interestCalculationPeriod period of interest calculation
	 * @return created debit account type
	 */
	DebitAccountType createDebitAccountType(
		BigDecimal interestOnBalance,
		Period interestCalculationPeriod);

	/**
	 * Method to change interest on balance of debit account with given id
	 *
	 * @param debitTypeId       id of debit account type
	 * @param interestOnBalance new interest on balance value
	 */
	void setInterestOnBalance(UUID debitTypeId, BigDecimal interestOnBalance);
}
