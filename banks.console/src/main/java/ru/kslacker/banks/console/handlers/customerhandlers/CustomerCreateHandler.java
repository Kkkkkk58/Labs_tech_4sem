package ru.kslacker.banks.console.handlers.customerhandlers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.builders.CustomerBuilder;
import ru.kslacker.banks.builders.CustomerLastNameBuilder;
import ru.kslacker.banks.builders.CustomerOptionalInformationBuilder;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.console.notifier.ConsoleNotifier;
import ru.kslacker.banks.entities.CustomerImpl;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.models.Address;
import ru.kslacker.banks.models.PassportData;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@ExtensionMethod(StreamExtensions.class)
public class CustomerCreateHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;

	public CustomerCreateHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("create");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		NoTransactionalBank bank = getBank();
		Customer customer = getCustomer();
		bank.registerCustomer(customer);
		writer.write("Successfully created customer " + customer.getId());
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
			.single(bank -> bank.getId().equals(bankId));
	}

	private Customer getCustomer() throws IOException {
		CustomerBuilder customerBuilder = CustomerImpl.getCustomerBuilder();
		writer.write("Enter first name: ");
		writer.flush();
		CustomerLastNameBuilder lastNameBuilder = customerBuilder
			.withFirstName(reader.readLine());
		writer.write("Enter last name: ");
		writer.flush();
		CustomerOptionalInformationBuilder optionalInfoBuilder = lastNameBuilder
			.withLastName(reader.readLine());
		setCustomerOptionalData(optionalInfoBuilder);

		return optionalInfoBuilder.build();
	}

	private void setCustomerOptionalData(CustomerOptionalInformationBuilder optionalInfoBuilder)
		throws IOException {
		optionalInfoBuilder.withNotifier(new ConsoleNotifier());
		setPassportData(optionalInfoBuilder);
		setAddress(optionalInfoBuilder);
	}

	private void setPassportData(CustomerOptionalInformationBuilder optionalInfoBuilder)
		throws IOException {
		writer.write("Enter passport data [optional]: ");
		writer.flush();
		String[] input = reader.readLine().split(" ");
		if (input.length != 2)
			return;

		PassportData passportData = new PassportData(LocalDate.parse(input[1]), input[0]);
		optionalInfoBuilder.withPassportData(passportData);
	}

	private void setAddress(CustomerOptionalInformationBuilder optionalInfoBuilder)
		throws IOException {
		writer.write("Enter address [optional]: ");
		writer.flush();
		Address address = new Address(reader.readLine());
		optionalInfoBuilder.withAddress(address);
	}
}
