package ru.kslacker.banks.console.handlers.bankhandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.extensions.StringExtensions;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@ExtensionMethod({StreamExtensions.class, StringExtensions.class})
public class BankChangeSuspiciousOperationsLimitHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;

	public BankChangeSuspiciousOperationsLimitHandler(CentralBank centralBank, BufferedReader reader, BufferedWriter writer) {
		super("susLimit");

		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		MoneyAmount suspiciousOperationsLimit = getSuspiciousOperationsLimit();

		bank.getAccountTypeManager().setSuspiciousOperationsLimit(suspiciousOperationsLimit);
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

	private MoneyAmount getSuspiciousOperationsLimit() throws IOException {
		writer.write("Enter suspicious operations limit: ");
		writer.flush();
		return reader.readLine().toMoneyAmount();
	}
}
