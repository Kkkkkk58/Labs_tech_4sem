package ru.kslacker.banks.console.handlers.bankhandlers;

import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.BankImpl;
import ru.kslacker.banks.entities.api.Bank;
import ru.kslacker.banks.models.AccountFactory;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.services.api.CentralBank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Clock;

public class BankCreateHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final AccountFactory accountFactory;
	private final Clock clock;
	private final BufferedReader reader;
	private final BufferedWriter writer;

	public BankCreateHandler(CentralBank centralBank, AccountFactory accountFactory, Clock clock, BufferedReader reader, BufferedWriter writer) {
		super("create");

		this.centralBank = centralBank;
		this.accountFactory = accountFactory;
		this.clock = clock;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		Bank bank = getBank();
		centralBank.registerBank(bank);
		writer.write("Successfully created new bank " + bank.getId());
		writer.newLine();
	}

	private Bank getBank() throws IOException {
		writer.write("Enter bank's name: ");
		String name = reader.readLine();
		writer.write("Enter suspicious operations limit: ");
		String limit = reader.readLine();
		MoneyAmount moneyLimit = limit.ToMoneyAmount();

		return new BankImpl(name, accountFactory, moneyLimit, clock);
	}

}
