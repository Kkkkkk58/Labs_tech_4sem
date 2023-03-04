package ru.kslacker.banks.console.handlers.api;

import ru.kslacker.banks.console.exceptions.HandlerException;

import java.io.IOException;

public abstract class HandlerImpl implements Handler {

	private final String handledRequest;

	protected HandlerImpl(String handledRequest) {
		this.handledRequest = handledRequest;
	}

	@Override
	public void handle(String... args) throws IOException {
		if (args.length < 1) {
			throw HandlerException.invalidRequestParametersLength(args.length);
		}
		if (!args[0].equalsIgnoreCase(handledRequest)) {
			throw HandlerException.invalidRequestType(handledRequest, args[0]);
		}

		handleImpl(args);
	}

	@Override
	public String getHandledRequest() {
		return handledRequest;
	}

	protected abstract void handleImpl(String... args) throws IOException;
}
