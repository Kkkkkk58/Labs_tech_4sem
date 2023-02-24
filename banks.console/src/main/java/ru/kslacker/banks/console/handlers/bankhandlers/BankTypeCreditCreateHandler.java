package ru.kslacker.banks.console.handlers.bankhandlers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

@ExtensionMethod(StreamExtensions.class)
public class BankTypeCreditCreateHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public BankTypeCreditCreateHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("create");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		CreditAccountType type = getType(bank);

		writer.write("Successfully created credit type " + type.getId());
		writer.newLine();
	}

	private NoTransactionalBank getBank() throws IOException {
		writer.write("Enter bank id: ");
		UUID bankId = UUID.fromString(reader.readLine());

		return centralBank
			.getBanks()
			.stream()
			.single(b -> b.getId().equals(bankId));
	}

	private CreditAccountType getType(NoTransactionalBank bank) throws IOException {
		writer.write("Enter debt limit: ");
		MoneyAmount debtLimit = reader.readLine().ToMoneyAmount();
		writer.write("Enter charge: ");
		MoneyAmount charge = reader.readLine().ToMoneyAmount();
		return bank.getAccountTypeManager().createCreditAccountType(debtLimit, charge);
	}
}
