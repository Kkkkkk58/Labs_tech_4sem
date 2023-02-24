package ru.kslacker.banks.console.handlers.operationshandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.console.representation.OperationInformationRepresentation;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@ExtensionMethod(StreamExtensions.class)
public class OperationDisplayHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public OperationDisplayHandler(CentralBank centralBank, BufferedReader reader, BufferedWriter writer) {
		super("display");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		ReadOnlyOperationInformation information = getOperationInformation();
		writer.write(new OperationInformationRepresentation(information).toString());
		writer.newLine();
		writer.flush();
	}

	private ReadOnlyOperationInformation getOperationInformation() throws IOException {
		writer.write("Enter operation id: ");
		writer.flush();
		UUID customerId = UUID.fromString(reader.readLine());

		return centralBank
			.getOperations()
			.stream()
			.single(operation -> operation.getId().equals(customerId));
	}

}
