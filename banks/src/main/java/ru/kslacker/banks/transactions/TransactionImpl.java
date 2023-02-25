package ru.kslacker.banks.transactions;

import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.models.OperationInformation;
import ru.kslacker.banks.transactions.states.NewTransactionState;
import ru.kslacker.banks.transactions.states.TransactionState;

public class TransactionImpl implements Transaction {

	private final Command command;
	private final OperationInformation information;
	private TransactionState state;

	/**
	 * Constructor of default transaction implementation
	 *
	 * @param information transaction details
	 * @param command     command to execute
	 */
	public TransactionImpl(OperationInformation information, Command command) {
		this.information = information;
		this.command = command;
		this.state = new NewTransactionState(this);
	}

	@Override
	public OperationInformation getInformation() {
		return information;
	}

	@Override
	public void setState(TransactionState state) {
		this.state = state;
	}

	@Override
	public void perform() {
		state.perform(command);
	}

	@Override
	public void cancel() {
		state.cancel(command);
	}
}
