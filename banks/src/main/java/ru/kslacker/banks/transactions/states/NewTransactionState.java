package ru.kslacker.banks.transactions.states;

import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.exceptions.TransactionStateException;
import ru.kslacker.banks.transactions.Transaction;

public class NewTransactionState extends TransactionStateBase {

	public NewTransactionState(Transaction transaction) {
		super(transaction);
	}

	@Override
	public void perform(Command command) {
		transaction.setState(new RunningTransactionState(transaction));
		transaction.getInformation().getAccount().execute(command, transaction);
	}

	@Override
	public void cancel(Command command) {
	 throw TransactionStateException.cancellingNewOperation(command.getId());
	}
}
