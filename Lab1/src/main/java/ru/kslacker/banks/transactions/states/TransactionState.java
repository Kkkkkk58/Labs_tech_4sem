package ru.kslacker.banks.transactions.states;

import ru.kslacker.banks.commands.Command;

public interface TransactionState {

	void perform(Command command);
	void cancel(Command command);
}
