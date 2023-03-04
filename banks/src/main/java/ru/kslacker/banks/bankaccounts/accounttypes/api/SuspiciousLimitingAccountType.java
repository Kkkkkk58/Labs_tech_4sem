package ru.kslacker.banks.bankaccounts.accounttypes.api;

import ru.kslacker.banks.models.MoneyAmount;

public interface SuspiciousLimitingAccountType extends AccountType {

	/**
	 * Method to get the limit on operations with suspicious accounts
	 *
	 * @return the limit
	 */
	MoneyAmount getSuspiciousAccountsOperationsLimit();

	/**
	 * Method to set the limit on operations with suspicious accounts
	 *
	 * @param limit new limit value
	 */
	void setSuspiciousAccountsOperationsLimit(MoneyAmount limit);
}
