package ru.kslacker.banks.transactions.states;

import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.exceptions.TransactionStateException;
import ru.kslacker.banks.transactions.Transaction;

public abstract class CompletedTransactionState extends TransactionStateBase {

	protected CompletedTransactionState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void perform(Command command) {
		throw TransactionStateException.performingAfterCompletion(command.getId());
	}
}
