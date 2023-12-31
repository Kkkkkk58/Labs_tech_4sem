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
public class WithdrawalOperationHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public WithdrawalOperationHandler(CentralBank centralBank, BufferedReader reader, BufferedWriter writer) {
		super("withdraw");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		UUID accountId = getAccountId();
		MoneyAmount moneyAmount = getMoneyAmount();

		ReadOnlyOperationInformation operationInformation = centralBank.withdraw(accountId, moneyAmount);
		writer.write("Transaction " + operationInformation.getId() + " was successful");
		writer.newLine();
		writer.flush();
	}

	private UUID getAccountId() throws IOException {
		writer.write("Enter account id: ");
		writer.flush();
		return UUID.fromString(reader.readLine());
	}

	private MoneyAmount getMoneyAmount() throws IOException {
		writer.write("Enter money amount: ");
		writer.flush();
		return reader.readLine().toMoneyAmount();
	}

}
