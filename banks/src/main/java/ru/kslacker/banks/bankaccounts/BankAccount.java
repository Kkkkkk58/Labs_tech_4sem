package ru.kslacker.banks.bankaccounts;

import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.transactions.Transaction;

public interface BankAccount extends CommandExecutingBankAccount {

	/**
	 * Method to withdraw given amount of money from the account
	 *
	 * @param transaction instance of current transaction
	 * @return withdrawn money amount
	 */
	MoneyAmount withdraw(Transaction transaction);

	/**
	 * Method to replenish account with given amount of money
	 *
	 * @param transaction instance of current transaction
	 */
	void replenish(Transaction transaction);
}
