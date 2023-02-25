package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.models.MoneyAmount;

public interface CreditAccountProvider {

	/**
	 * Method to create credit account
	 *
	 * @param type     credit account type
	 * @param customer holder of the account
	 * @param balance  initial balance of the account
	 * @return information about created credit account
	 */
	UnmodifiableBankAccount createCreditAccount(
		AccountType type,
		Customer customer,
		MoneyAmount balance);
}
