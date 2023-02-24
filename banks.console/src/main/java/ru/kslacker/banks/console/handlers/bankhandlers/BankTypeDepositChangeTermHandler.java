package ru.kslacker.banks.console.handlers.bankhandlers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Period;
import java.util.UUID;

@ExtensionMethod(StreamExtensions.class)
public class BankTypeDepositChangeTermHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public BankTypeDepositChangeTermHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("term");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		UUID typeId = getTypeId();
		Period term = getTerm();

		bank.getAccountTypeManager().setDepositTerm(typeId, term);
	}

	private NoTransactionalBank getBank() throws IOException {
		writer.write("Enter bank id: ");
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

	private Period getTerm() throws IOException {
		writer.write("Enter term: ");
		return Period.parse(reader.readLine());
	}

}
