package ru.kslacker.banks.bankaccounts.accounttypes.api;

import ru.kslacker.banks.models.InterestOnBalancePolicy;

public interface InterestGradesAccountType extends InterestCalculationAccountType {

	/**
	 * Method to get set of rules to determine interest on balance percent based on initial balance
	 * of an account
	 *
	 * @return interest on balance policy
	 */
	InterestOnBalancePolicy getInterestOnBalancePolicy();

	/**
	 * Method to set interest on balance policy
	 *
	 * @param policy new set of rules to determine interest on balance percent based on initial
	 *               balance of an account
	 */
	void setInterestOnBalancePolicy(InterestOnBalancePolicy policy);
}
