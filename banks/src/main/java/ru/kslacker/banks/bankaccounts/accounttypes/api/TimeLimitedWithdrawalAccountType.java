package ru.kslacker.banks.bankaccounts.accounttypes.api;


import java.time.Period;

public interface TimeLimitedWithdrawalAccountType extends AccountType {

	/**
	 * Method to get the limit on withdrawal of money
	 *
	 * @return time limit
	 */
	Period getDepositTerm();

	/**
	 * Method to set the limit on withdrawal of money
	 *
	 * @param depositTerm new value of time limit
	 */
	void setDepositTerm(Period depositTerm);
}
