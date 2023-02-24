package ru.kslacker.banks.console.handlers.operationshandlers;

import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import ru.kslacker.banks.services.api.CentralBank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

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
	}

	private UUID getSenderAccountId() throws IOException {
		writer.write("Enter sender account id: ");
		return UUID.fromString(reader.readLine());
	}

	private UUID getReceiverAccountId() throws IOException {
		writer.write("Enter receiver account id: ");
		return UUID.fromString(reader.readLine());
	}

	private MoneyAmount getMoneyAmount() throws IOException {
		writer.write("Enter money amount: ");
		return reader.readLine().ToMoneyAmount();
	}
}
