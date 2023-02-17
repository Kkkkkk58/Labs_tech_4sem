package ru.kslacker.banks.commands;

import lombok.AllArgsConstructor;
import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.CommandExecutingBankAccount;
import ru.kslacker.banks.transactions.Transaction;

@AllArgsConstructor
public class TransferCommand extends Command {

	private final CommandExecutingBankAccount receiver;

	@Override
	public void execute(BankAccount bankAccount, Transaction transaction) {
		bankAccount.withdraw(transaction);
		receiver.execute(new ReplenishmentCommand(), transaction);
	}

	@Override
	public void undo(Transaction transaction) {
		receiver.execute(new TransferCommand(transaction.getInformation().getAccount()), transaction);
	}
}
