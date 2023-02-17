package ru.kslacker.banks.commands;

import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.transactions.Transaction;

public class ReplenishmentCommand extends Command {

	@Override
	public void execute(BankAccount bankAccount, Transaction transaction) {
		bankAccount.replenish(transaction);
	}

	@Override
	public void undo(Transaction transaction) {
		transaction.getInformation().getAccount().execute(new WithdrawalCommand(), transaction);
	}
}
