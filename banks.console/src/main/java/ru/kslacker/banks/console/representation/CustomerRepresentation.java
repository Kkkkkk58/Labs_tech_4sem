package ru.kslacker.banks.console.representation;

import ru.kslacker.banks.entities.api.Customer;
import java.util.UUID;

public class CustomerRepresentation {

	private final String firstName;
	private final String lastName;
	private final UUID id;
	private final boolean isVerified;

	public CustomerRepresentation(Customer customer) {
		this.firstName = customer.getFirstName();
		this.lastName = customer.getLastName();
		this.id = customer.getId();
		this.isVerified = customer.isVerified();
	}

	@Override
	public String toString() {
		return
			"Customer " + firstName + " " + lastName + " [" + id + "]\n" +
			"Is verified: " + isVerified;
	}
}
