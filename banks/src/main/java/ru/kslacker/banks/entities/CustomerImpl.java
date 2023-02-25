package ru.kslacker.banks.entities;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;
import ru.kslacker.banks.builders.CustomerBuilder;
import ru.kslacker.banks.builders.CustomerLastNameBuilder;
import ru.kslacker.banks.builders.CustomerOptionalInformationBuilder;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.entities.api.CustomerNotifier;
import ru.kslacker.banks.entities.api.CustomerNotifierDecoratorBuilder;
import ru.kslacker.banks.eventargs.CustomerAccountChangesEventArgs;
import ru.kslacker.banks.exceptions.CustomerException;
import ru.kslacker.banks.models.Address;
import ru.kslacker.banks.models.PassportData;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CustomerImpl implements Customer {

	@Getter(AccessLevel.NONE)
	private Address address;
	@Getter(AccessLevel.NONE)
	private PassportData passportData;
	@Getter(AccessLevel.NONE)
	private CustomerNotifier notifier;
	@Include
	private final UUID id;
	private final String firstName;
	private final String lastName;

	/**
	 * Constructor of default customer implementation
	 *
	 * @param id           customer id
	 * @param firstName    customer first name
	 * @param lastName     customer last name
	 * @param notifier     customer notifier
	 * @param address      customer address
	 * @param passportData customer passport data
	 */
	private CustomerImpl(
		UUID id,
		String firstName,
		String lastName,
		CustomerNotifier notifier,
		Address address,
		PassportData passportData) {

		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.notifier = notifier;
		this.address = address;
		this.passportData = passportData;
	}

	/**
	 * Method to get instance of customer builder
	 *
	 * @return customer builder
	 */
	public static CustomerBuilder getCustomerBuilder() {
		return new CustomerBuilderImpl();
	}

	@Override
	public boolean isVerified() {
		return address != null && passportData != null;
	}

	@Override
	public void addNotifier(CustomerNotifierDecoratorBuilder notifierDecoratorBuilder) {
		if (notifier == null) {
			throw CustomerException.notifierIsNotSet(id);
		}

		notifier = notifierDecoratorBuilder.withWrapped(notifier).build();
	}

	@Override
	public void update(Object sender,
		CustomerAccountChangesEventArgs customerAccountChangesEventArgs) {
		if (notifier != null) {
			notifier.send(customerAccountChangesEventArgs.message());
		}
	}

	private static class CustomerBuilderImpl implements
		CustomerBuilder,
		CustomerLastNameBuilder,
		CustomerOptionalInformationBuilder {

		private String firstName;
		private String lastName;
		private Address address;
		private PassportData passportData;
		private CustomerNotifier notifier;

		@Override
		public CustomerLastNameBuilder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		@Override
		public CustomerOptionalInformationBuilder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		@Override
		public CustomerOptionalInformationBuilder withAddress(Address address) {
			this.address = address;
			return this;
		}

		@Override
		public CustomerOptionalInformationBuilder withPassportData(PassportData passportData) {
			this.passportData = passportData;
			return this;
		}

		@Override
		public CustomerOptionalInformationBuilder withNotifier(CustomerNotifier notifier) {
			this.notifier = notifier;
			return this;
		}

		public Customer build() {

			if (firstName.isEmpty()) {
				throw new IllegalArgumentException();
			}
			if (lastName.isEmpty()) {
				throw new IllegalArgumentException();
			}

			Customer customer = new CustomerImpl(UUID.randomUUID(), firstName, lastName, notifier,
				address, passportData);
			reset();

			return customer;
		}

		private void reset() {
			firstName = null;
			lastName = null;
			address = null;
			passportData = null;
			notifier = null;
		}
	}
}
