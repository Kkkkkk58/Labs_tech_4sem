package ru.kslacker.banks.console.handlers.operationshandlers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.extensions.StringExtensions;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import ru.kslacker.banks.services.api.CentralBank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

@ExtensionMethod(StringExtensions.class)
public class TransferOperationHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public TransferOperationHandler(CentralBank centralBank, BufferedReader reader, BufferedWriter writer) {
		super("transfer");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		UUID fromAccountId = getSenderAccountId();
		UUID toAccountId = getReceiverAccountId();
		MoneyAmount moneyAmount = getMoneyAmount();

		ReadOnlyOperationInformation operationInformation = centralBank.transfer(fromAccountId, toAccountId, moneyAmount);
		writer.write("Transaction " + operationInformation.getId() + " was successful");
		writer.newLine();
		writer.flush();
	}

	private UUID getSenderAccountId() throws IOException {
		writer.write("Enter sender account id: ");
		writer.flush();
		return UUID.fromString(reader.readLine());
	}

	private UUID getReceiverAccountId() throws IOException {
		writer.write("Enter receiver account id: ");
		writer.flush();
		return UUID.fromString(reader.readLine());
	}

	private MoneyAmount getMoneyAmount() throws IOException {
		writer.write("Enter money amount: ");
		writer.flush();
		return reader.readLine().toMoneyAmount();
	}
}
