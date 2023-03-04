package ru.kslacker.banks.console.handlers.customerhandlers;

import ru.kslacker.banks.console.handlers.api.CompositeHandlerImpl;

public class CustomerHandler extends CompositeHandlerImpl {

	public CustomerHandler() {
		super("client");
	}
}
