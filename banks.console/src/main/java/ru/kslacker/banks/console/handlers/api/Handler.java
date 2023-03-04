package ru.kslacker.banks.console.handlers.api;

import java.io.IOException;

public interface Handler {

	String getHandledRequest();
	void handle(String... args) throws IOException;
}
