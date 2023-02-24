package ru.kslacker.banks.console.handlers.accounthandlers;

import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.services.api.CentralBank;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class AccountCreateCreditHandler extends AccountCreateHandlerBase {

	private final BufferedWriter writer;

	public AccountCreateCreditHandler(CentralBank centralBank, BufferedWriter writer, BufferedReader reader)
	{
		super("credit", centralBank, writer, reader);
		this.writer = writer;
	}

	@Override
	protected void createAccount(
		NoTransactionalBank bank,
		AccountType type,
		Customer customer,
		MoneyAmount balance) throws IOException {
		UnmodifiableBankAccount account = bank.createCreditAccount(type, customer, balance);
		writer.write("Successfully created new credit account " + account.getId());
		writer.newLine();
	}

}
