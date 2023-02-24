package ru.kslacker.banks.console.handlers.customerhandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.console.representation.CustomerRepresentation;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@ExtensionMethod(StreamExtensions.class)
public class CustomerInformationDisplayHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public CustomerInformationDisplayHandler(CentralBank centralBank, BufferedReader reader, BufferedWriter writer) {
		super("display");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		Customer customer = getCustomer();
		writer.write(new CustomerRepresentation(customer).toString());
		writer.newLine();
	}

	private Customer getCustomer() throws IOException {
		writer.write("Enter customer id: ");
		UUID customerId = UUID.fromString(reader.readLine());

		return centralBank
			.getBanks()
			.stream()
			.flatMap(bank -> bank.getCustomers().stream())
			.distinct()
			.single(customer -> customer.getId().equals(customerId));
	}

}
