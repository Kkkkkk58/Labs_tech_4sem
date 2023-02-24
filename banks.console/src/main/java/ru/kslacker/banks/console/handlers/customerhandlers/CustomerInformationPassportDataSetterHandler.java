package ru.kslacker.banks.console.handlers.customerhandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.models.PassportData;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@ExtensionMethod(StreamExtensions.class)
public class CustomerInformationPassportDataSetterHandler extends HandlerImpl {

	private final CentralBank centralBank;
	private final BufferedReader reader;
	private final BufferedWriter writer;


	public CustomerInformationPassportDataSetterHandler(CentralBank centralBank, BufferedReader reader,
		BufferedWriter writer) {
		super("passport");
		this.centralBank = centralBank;
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	protected void handleImpl(String... args) throws IOException {
		Customer customer = getCustomer();
		PassportData passportData = getPassportData();
		customer.setPassportData(passportData);

		writer.write("Set new passport data for customer " + customer.getId());
		writer.newLine();
		writer.flush();
	}

	private Customer getCustomer() throws IOException {
		writer.write("Enter customer id: ");
		writer.flush();
		UUID customerId = UUID.fromString(reader.readLine());

		return centralBank
			.getBanks()
			.stream()
			.flatMap(bank -> bank.getCustomers().stream())
			.distinct()
			.single(customer -> customer.getId().equals(customerId));
	}

	private PassportData getPassportData() throws IOException {
		writer.write("Enter passport of number: ");
		writer.flush();
		String number = reader.readLine();
		writer.write("Enter date of issue: ");
		writer.flush();
		LocalDate dateOfIssue = LocalDate.parse(reader.readLine());

		return new PassportData(dateOfIssue, number);
	}
}
