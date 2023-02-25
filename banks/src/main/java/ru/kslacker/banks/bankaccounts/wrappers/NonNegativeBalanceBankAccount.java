package ru.kslacker.banks.bankaccounts.wrappers;

import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.exceptions.TransactionException;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.transactions.Transaction;

public class NonNegativeBalanceBankAccount extends BankAccountWrapper {

	/**
	 * Bank account that doesn't support negative balances
	 *
	 * @param wrapped wrapped bank account
	 */
	public NonNegativeBalanceBankAccount(BankAccount wrapped) {
		super(wrapped);
	}

	@Override
	public MoneyAmount withdraw(Transaction transaction) {

		if (getBalance().compareTo(transaction.getInformation().getOperatedAmount()) < 0) {
			throw TransactionException.negativeBalance();
		}

		return super.withdraw(transaction);
	}
}
