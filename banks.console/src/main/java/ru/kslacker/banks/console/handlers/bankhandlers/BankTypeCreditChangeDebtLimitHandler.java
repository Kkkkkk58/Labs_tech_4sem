package ru.kslacker.banks.console.handlers.bankhandlers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.extensions.StringExtensions;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

@ExtensionMethod({StreamExtensions.class, StringExtensions.class})
public class BankTypeCreditChangeDebtLimitHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public BankTypeCreditChangeDebtLimitHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("debtLimit");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		UUID typeId = getTypeId();
		MoneyAmount debtLimit = getDebtLimit();

		bank.getAccountTypeManager().setDebtLimit(typeId, debtLimit);
	}

	private NoTransactionalBank getBank() throws IOException {
		writer.write("Enter bank id: ");
		writer.flush();
		UUID bankId = UUID.fromString(reader.readLine());

		return centralBank
			.getBanks()
			.stream()
			.single(b -> b.getId().equals(bankId));
	}

	private UUID getTypeId() throws IOException {
		writer.write("Enter type id: ");
		return UUID.fromString(reader.readLine());
	}

	private MoneyAmount getDebtLimit() throws IOException {
		writer.write("Enter debt limit: ");
		writer.flush();
		return reader.readLine().toMoneyAmount();
	}
}
