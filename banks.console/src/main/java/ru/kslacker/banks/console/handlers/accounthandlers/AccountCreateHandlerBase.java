package ru.kslacker.banks.console.handlers.accounthandlers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.console.extensions.StringExtensions;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

@ExtensionMethod({StreamExtensions.class, StringExtensions.class})
public abstract class AccountCreateHandlerBase extends HandlerImpl {

	private final BufferedWriter writer;
	private final BufferedReader reader;
	private final CentralBank centralBank;

	protected AccountCreateHandlerBase(String handledRequest, CentralBank centralBank, BufferedWriter writer, BufferedReader reader) {
		super(handledRequest);

		this.centralBank = centralBank;
		this.writer = writer;
		this.reader = reader;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		AccountType type = getType(bank);
		Customer customer = getCustomer(bank);
		MoneyAmount balance = getBalance();

		createAccount(bank, type, customer, balance);
	}

	protected abstract void createAccount(
		NoTransactionalBank bank,
		AccountType type,
		Customer customer,
		MoneyAmount balance) throws IOException;

	private NoTransactionalBank getBank() throws IOException {
		writer.write("Enter bank id: ");
		writer.flush();
		UUID bankId = UUID.fromString(reader.readLine());

		return centralBank
			.getBanks()
			.stream()
			.single(b -> b.getId().equals(bankId));
	}

	private AccountType getType(NoTransactionalBank bank) throws IOException {
		writer.write("Enter account type id: ");
		UUID typeId = UUID.fromString(reader.readLine());
		return bank.getAccountTypeManager().getAccountType(typeId);
	}

	private Customer getCustomer(NoTransactionalBank bank) throws IOException {
		writer.write("Enter customer id: ");
		writer.flush();
		UUID clientId = UUID.fromString(reader.readLine());

		return bank
			.getCustomers()
			.stream()
			.single(c -> c.getId().equals(clientId));
	}

	private MoneyAmount getBalance() throws IOException {
		writer.write("Enter initial balance [optional]: ");
		writer.flush();
		String input = reader.readLine();
		if (input.isEmpty()) {
			return null;
		}

		return input.toMoneyAmount();
	}

}
