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
public class BankTypeDepositChangeInterestCalculationPeriodHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public BankTypeDepositChangeInterestCalculationPeriodHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("period");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		UUID typeId = getTypeId();
		Period period = getInterestCalculationPeriod();

		bank.getAccountTypeManager().setInterestCalculationPeriod(typeId, period);
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
		writer.flush();
		return UUID.fromString(reader.readLine());
	}

	private Period getInterestCalculationPeriod() throws IOException {
		writer.write("Enter interest calculation period: ");
		writer.flush();
		return Period.parse(reader.readLine());
	}

}
