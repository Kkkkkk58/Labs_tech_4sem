package ru.kslacker.banks.console.handlers.operationshandlers;

import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.services.api.CentralBank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

public class OperationCancellationHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;

	public OperationCancellationHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("cancel");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		UUID operationId = getOperationId();
		centralBank.cancelTransaction(operationId);
		writer.write("Transaction " + operationId + " cancellation was successful");
		writer.newLine();
	}

	private UUID getOperationId() throws IOException {
		writer.write("Enter operation id: ");
		return UUID.fromString(reader.readLine());
	}
}
