package ru.kslacker.banks.accounttypemanager.api;

import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.models.MoneyAmount;

import java.util.UUID;

public interface CreditTypeProvider extends SuspiciousLimitingTypeProvider {

	/**
	 * Method to create new credit type
	 *
	 * @param debtLimit limit of debt, after reaching it the charge is taken
	 * @param charge    amount of money taken after reaching debt limit
	 * @return created credit account type
	 */
	CreditAccountType createCreditAccountType(MoneyAmount debtLimit, MoneyAmount charge);

	/**
	 * Method to change debt limit of specific credit account type
	 *
	 * @param creditTypeId id of account type
	 * @param debtLimit    new value of debt limit
	 */
	void setDebtLimit(UUID creditTypeId, MoneyAmount debtLimit);

	/**
	 * Method to change charge of specific credit account type
	 *
	 * @param creditTypeId id of account type
	 * @param charge       new value of charge
	 */
	void setCharge(UUID creditTypeId, MoneyAmount charge);
}
