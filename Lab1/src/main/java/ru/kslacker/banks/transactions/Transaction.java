package ru.kslacker.banks.transactions;

import ru.kslacker.banks.models.OperationInformation;
import ru.kslacker.banks.transactions.states.TransactionState;

public interface Transaction {

	OperationInformation getInformation();
	void setState(TransactionState state);
	void perform();
	void cancel();
}
