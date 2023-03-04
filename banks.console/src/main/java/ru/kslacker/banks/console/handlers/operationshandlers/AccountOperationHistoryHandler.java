package ru.kslacker.banks.console.handlers.operationshandlers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.console.representation.OperationInformationRepresentation;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

@ExtensionMethod(StreamExtensions.class)
public class AccountOperationHistoryHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;

	public AccountOperationHistoryHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("account");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		UUID accountId = getAccountId();

		Collection<ReadOnlyOperationInformation> operationHistory = centralBank
			.getBanks()
			.stream()
			.single(bank -> bank.findAccount(accountId).isPresent())
            .getAccount(accountId)
			.getOperationHistory();

		for (ReadOnlyOperationInformation operationInformation : operationHistory)
		{
			writer.write(new OperationInformationRepresentation(operationInformation).toString());
			writer.newLine();
		}
		writer.flush();
	}

	private UUID getAccountId() throws IOException {
		writer.write("Enter account id: ");
		writer.flush();
		return UUID.fromString(reader.readLine());
	}
}
