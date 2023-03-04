package ru.kslacker.banks.console.handlers.bankhandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Period;
import java.util.UUID;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@ExtensionMethod(StreamExtensions.class)
public class BankTypeDebitCreateHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public BankTypeDebitCreateHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("create");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		DebitAccountType type = getType(bank);

		writer.write("Successfully created debit type " + type.getId());
		writer.newLine();
		writer.flush();
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

	private DebitAccountType getType(NoTransactionalBank bank) throws IOException {
		writer.write("Enter interest on balance: ");
		writer.flush();
		BigDecimal interestOnBalance = new BigDecimal(reader.readLine());
		writer.write("Enter interest calculation period: ");
		writer.flush();
		Period period = Period.parse(reader.readLine());
		return bank.getAccountTypeManager().createDebitAccountType(interestOnBalance, period);
	}

}
