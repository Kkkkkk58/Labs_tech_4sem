package ru.kslacker.banks.console.handlers.customerhandlers;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.models.Address;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.UUID;

@ExtensionMethod(StreamExtensions.class)
public class CustomerInformationAddressSetterHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public CustomerInformationAddressSetterHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("address");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		Customer customer = getCustomer();
		Address address = getAddress();
		customer.setAddress(address);

		writer.write("Set new address for customer " + customer.getId());
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

	private Address getAddress() throws IOException {
		writer.write("Enter address: ");
		return new Address(reader.readLine());
	}
}
