package ru.kslacker.banks.console.handlers.bankhandlers;

import ru.kslacker.banks.console.handlers.api.CompositeHandlerImpl;

public class BankTypeDebitHandler extends CompositeHandlerImpl {

	public BankTypeDebitHandler() {
		super("debit");
	}
}
