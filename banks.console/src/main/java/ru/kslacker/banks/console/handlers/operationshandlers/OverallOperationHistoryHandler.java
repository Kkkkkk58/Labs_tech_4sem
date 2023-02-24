package ru.kslacker.banks.console.handlers.operationshandlers;

import java.io.BufferedWriter;
import java.io.IOException;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.console.representation.OperationInformationRepresentation;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import ru.kslacker.banks.services.api.CentralBank;

public class OverallOperationHistoryHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedWriter writer;


	public OverallOperationHistoryHandler(CentralBank centralBank, BufferedWriter writer) {
		super("overall");
		this.centralBank = centralBank;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		for (ReadOnlyOperationInformation info : centralBank.getOperations()) {
			writer.write(new OperationInformationRepresentation(info).toString());
			writer.newLine();
		}
	}
}
