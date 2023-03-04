package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.models.MoneyAmount;

public interface DebitAccountProvider {

	/**
	 * Method to create new debit account
	 *
	 * @param type     debit account type
	 * @param customer account holder
	 * @param balance  initial balance of the account
	 * @return created account info
	 */
	UnmodifiableBankAccount createDebitAccount(
		AccountType type,
		Customer customer,
		MoneyAmount balance);
}
