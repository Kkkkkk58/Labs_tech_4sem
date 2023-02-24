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

public class AccountCreateDebitHandler extends AccountCreateHandlerBase {

	private final BufferedWriter writer;

	public AccountCreateDebitHandler(CentralBank centralBank, BufferedWriter writer, BufferedReader reader) {
		super("debit", centralBank, writer, reader);
		this.writer = writer;
	}

	@Override
	protected void createAccount(NoTransactionalBank bank, AccountType type, Customer customer, MoneyAmount balance)
		throws IOException {
		UnmodifiableBankAccount account = bank.createDebitAccount(type, customer, balance);
		writer.write("Successfully created new debit account " + account.getId());
		writer.newLine();
		writer.flush();
	}

}
