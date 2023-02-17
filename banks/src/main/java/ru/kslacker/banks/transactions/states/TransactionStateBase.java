package ru.kslacker.banks.transactions.states;

import ru.kslacker.banks.transactions.Transaction;

public abstract class TransactionStateBase implements TransactionState {

	protected final Transaction transaction;

	protected TransactionStateBase(Transaction transaction) {
		this.transaction = transaction;
	}
}
