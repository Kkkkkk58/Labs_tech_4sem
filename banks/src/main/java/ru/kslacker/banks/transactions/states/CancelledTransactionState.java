package ru.kslacker.banks.transactions.states;

import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.exceptions.TransactionStateException;
import ru.kslacker.banks.transactions.Transaction;

public class CancelledTransactionState extends CompletedTransactionState {

	public CancelledTransactionState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void cancel(Command command) {
		throw TransactionStateException.multipleCancelling(command.getId());
	}
}
