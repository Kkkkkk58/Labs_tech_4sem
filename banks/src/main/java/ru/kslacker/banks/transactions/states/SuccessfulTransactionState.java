package ru.kslacker.banks.transactions.states;

import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.transactions.Transaction;

public class SuccessfulTransactionState extends CompletedTransactionState {

	public SuccessfulTransactionState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void cancel(Command command) {
		command.undo(transaction);
		transaction.setState(new CancelledTransactionState(transaction));
	}
}
