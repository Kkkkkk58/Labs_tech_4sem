package ru.kslacker.banks.transactions.states;

import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.exceptions.TransactionStateException;
import ru.kslacker.banks.transactions.Transaction;

public class FailedTransactionState extends CompletedTransactionState {

	public FailedTransactionState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void cancel(Command command) {
		throw TransactionStateException.cancellingFailedOperation(command.getId());
	}
}
