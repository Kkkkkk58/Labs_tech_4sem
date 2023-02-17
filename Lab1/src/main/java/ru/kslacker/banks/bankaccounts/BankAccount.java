package ru.kslacker.banks.bankaccounts;

import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.transactions.Transaction;

public interface BankAccount extends CommandExecutingBankAccount {

	MoneyAmount withdraw(Transaction transaction);
	void replenish(Transaction transaction);
}
