package ru.kslacker.banks.accounttypemanager.api;

import ru.kslacker.banks.models.MoneyAmount;

public interface SuspiciousLimitingTypeProvider {

	/**
	 * Method to get the limit on transactions from suspicious accounts
	 *
	 * @return amount of money - limit on transactions
	 */
	MoneyAmount getSuspiciousAccountsOperationsLimit();

	/**
	 * Method to set the limit on transactions from suspicious accounts
	 *
	 * @param limit new value of limit
	 */
	void setSuspiciousOperationsLimit(MoneyAmount limit);
}
