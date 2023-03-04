package ru.kslacker.banks.models;

import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.entities.api.Customer;

public interface AccountFactory {

	/**
	 * Method to create new debit account with given parameters
	 *
	 * @param type     account type
	 * @param customer account holder
	 * @param balance  initial balance
	 * @return created account
	 */
	BankAccount createDebitAccount(DebitAccountType type, Customer customer, MoneyAmount balance);

	/**
	 * Method to create new debit account with given parameters
	 *
	 * @param type     account type
	 * @param customer account holder
	 * @param balance  initial balance
	 * @return created account
	 */
	BankAccount createDepositAccount(DepositAccountType type, Customer customer,
		MoneyAmount balance);

	/**
	 * Method to create new debit account with given parameters
	 *
	 * @param type     account type
	 * @param customer account holder
	 * @param balance  initial balance
	 * @return created account
	 */
	BankAccount createCreditAccount(CreditAccountType type, Customer customer, MoneyAmount balance);
}
