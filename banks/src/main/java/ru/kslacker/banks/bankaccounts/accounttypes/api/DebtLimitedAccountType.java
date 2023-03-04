package ru.kslacker.banks.bankaccounts.accounttypes.api;

import ru.kslacker.banks.models.MoneyAmount;

public interface DebtLimitedAccountType {

	/**
	 * Method to get debt limit
	 *
	 * @return value of debt limit
	 */
	MoneyAmount getDebtLimit();

	/**
	 * Method to set new debt limit
	 *
	 * @param debtLimit new value of debt limit
	 */
	void setDebtLimit(MoneyAmount debtLimit);
}
