package ru.kslacker.banks.console.handlers.bankhandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@ExtensionMethod(StreamExtensions.class)
public class BankTypeDebitChangeInterestHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public BankTypeDebitChangeInterestHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("interest");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		UUID typeId = getTypeId();
		BigDecimal interestOnBalance = getInterestOnBalance();

		bank.getAccountTypeManager().setInterestOnBalance(typeId, interestOnBalance);
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

	private BigDecimal getInterestOnBalance() throws IOException {
		writer.write("Enter interest on balance: ");
		return new BigDecimal(reader.readLine());
	}
}
