package ru.kslacker.banks.accounttypemanager.api;


import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.models.InterestOnBalancePolicy;

import java.time.Period;
import java.util.UUID;

public interface DepositTypeProvider extends
	SuspiciousLimitingTypeProvider,
	InterestCalculatingTypeProvider {

	/**
	 * Method to create new deposit account type with given attributes
	 *
	 * @param depositTerm               deposit term
	 * @param interestOnBalancePolicy   set of rules to determine interest on balance based on
	 *                                  initial balance
	 * @param interestCalculationPeriod period of interest calculation
	 * @return created deposit account type
	 */
	DepositAccountType createDepositAccountType(
		Period depositTerm,
		InterestOnBalancePolicy interestOnBalancePolicy,
		Period interestCalculationPeriod);

	/**
	 * Method to change interest on balance policy of account type with given id
	 *
	 * @param depositTypeId id of deposit account type
	 * @param policy        new interest on balance policy
	 */
	void setInterestOnBalancePolicy(UUID depositTypeId, InterestOnBalancePolicy policy);

	/**
	 * Method to change deposit term of account type with given id
	 *
	 * @param depositTypeId d of deposit account type
	 * @param depositTerm   new deposit term
	 */
	void setDepositTerm(UUID depositTypeId, Period depositTerm);
}
