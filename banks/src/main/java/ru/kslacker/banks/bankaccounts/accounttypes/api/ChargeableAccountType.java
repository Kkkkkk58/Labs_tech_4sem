package ru.kslacker.banks.bankaccounts.accounttypes.api;

import ru.kslacker.banks.models.MoneyAmount;

public interface ChargeableAccountType {

	/**
	 * Method to get charge of credit account type
	 *
	 * @return charge of credit account type
	 */
	MoneyAmount getCharge();

	/**
	 * Method to set charge of credit account type
	 *
	 * @param charge new value of charge
	 */
	void setCharge(MoneyAmount charge);
}
