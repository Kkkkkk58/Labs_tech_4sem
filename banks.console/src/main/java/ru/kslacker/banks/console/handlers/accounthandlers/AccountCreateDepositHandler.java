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

public class AccountCreateDepositHandler extends AccountCreateHandlerBase {

	private final BufferedWriter writer;

	public AccountCreateDepositHandler(CentralBank bank, BufferedWriter writer, BufferedReader reader) {
		super("deposit", bank, writer, reader);
		this.writer = writer;
	}

	@Override
	public void createAccount(NoTransactionalBank bank, AccountType type, Customer customer, MoneyAmount balance)
		throws IOException {
		UnmodifiableBankAccount account = bank.createDepositAccount(type, customer, balance);
		writer.write("Successfully created new deposit account " + account.getId());
		writer.newLine();
	}
}
