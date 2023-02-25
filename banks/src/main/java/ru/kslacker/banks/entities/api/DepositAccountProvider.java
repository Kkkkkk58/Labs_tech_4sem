package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.models.MoneyAmount;

public interface DepositAccountProvider {

	/**
	 * Method to create new deposit account
	 *
	 * @param type     deposit account type
	 * @param customer account holder
	 * @param balance  initial balance of the account
	 * @return information about created account
	 */
	UnmodifiableBankAccount createDepositAccount(
		AccountType type,
		Customer customer,
		MoneyAmount balance);
}
