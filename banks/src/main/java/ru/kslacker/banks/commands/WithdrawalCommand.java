package ru.kslacker.banks.commands;

import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.transactions.Transaction;

public class WithdrawalCommand extends Command {

	@Override
	public void execute(BankAccount bankAccount, Transaction transaction) {
		bankAccount.withdraw(transaction);
	}

	@Override
	public void undo(Transaction transaction) {
		transaction.getInformation().getAccount().execute(new ReplenishmentCommand(), transaction);
	}
}
