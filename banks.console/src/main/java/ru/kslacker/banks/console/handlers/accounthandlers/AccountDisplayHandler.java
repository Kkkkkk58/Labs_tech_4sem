package ru.kslacker.banks.console.handlers.accounthandlers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.console.representation.AccountRepresentation;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

@ExtensionMethod(StreamExtensions.class)
public class AccountDisplayHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedWriter writer;
	private final BufferedReader reader;

	public AccountDisplayHandler(CentralBank centralBank, BufferedWriter writer, BufferedReader reader) {
		super("display");

		this.centralBank = centralBank;
		this.writer = writer;
		this.reader = reader;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		UnmodifiableBankAccount account = getAccount();
		writer.write(new AccountRepresentation(account).toString());
		writer.newLine();
		writer.flush();
	}

	private UnmodifiableBankAccount getAccount() throws IOException {
		writer.write("Enter account id: ");
		writer.flush();
		UUID accountId = UUID.fromString(reader.readLine());

		return centralBank.getBanks()
			.stream()
			.single(bank -> bank.findAccount(accountId).isPresent())
			.getAccount(accountId);
	}

}
