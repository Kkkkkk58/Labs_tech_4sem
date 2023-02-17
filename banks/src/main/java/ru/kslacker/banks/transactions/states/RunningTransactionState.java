package ru.kslacker.banks.transactions.states;

import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.exceptions.TransactionStateException;
import ru.kslacker.banks.transactions.Transaction;

public class RunningTransactionState extends TransactionStateBase {

	public RunningTransactionState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void perform(Command command) {
		throw TransactionStateException.alreadyRunningOperation(command.getId());
	}

	@Override
	public void cancel(Command command) {
		transaction.setState(new FailedTransactionState(transaction));
	}
}
