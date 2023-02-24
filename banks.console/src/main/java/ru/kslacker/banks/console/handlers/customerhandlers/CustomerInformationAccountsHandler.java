package ru.kslacker.banks.console.handlers.customerhandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;
import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.console.representation.AccountRepresentation;
import ru.kslacker.banks.services.api.CentralBank;

public class CustomerInformationAccountsHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public CustomerInformationAccountsHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("accounts");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		UUID customerId = getCustomerId();

		for (UnmodifiableBankAccount account : getCustomerAccounts(customerId))
		{
			writer.write(new AccountRepresentation(account).toString());
			writer.newLine();
		}
	}

	private Collection<UnmodifiableBankAccount> getCustomerAccounts(UUID customerId)
	{
		return centralBank
			.getBanks()
			.stream()
			.flatMap(bank -> bank.getAccounts(customerId).stream())
			.toList();
	}

	private UUID getCustomerId() throws IOException {
		writer.write("Enter customer id: ");
		return UUID.fromString(reader.readLine());
	}
}
