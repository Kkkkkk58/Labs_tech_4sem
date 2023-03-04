package ru.kslacker.banks.builders;

import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.entities.api.CustomerNotifier;
import ru.kslacker.banks.models.Address;
import ru.kslacker.banks.models.PassportData;

public interface CustomerOptionalInformationBuilder {

	/**
	 * Method to set client's address
	 *
	 * @param address client's address
	 * @return builder state to set other optional info or build client
	 */
	CustomerOptionalInformationBuilder withAddress(Address address);

	/**
	 * Method to set client's passport data
	 *
	 * @param passportData client's passport data
	 * @return builder state to set other optional info or build client
	 */
	CustomerOptionalInformationBuilder withPassportData(PassportData passportData);

	/**
	 * Method to set client notifier
	 *
	 * @param notifier client notifier
	 * @return builder state to set other optional info or build client
	 */
	CustomerOptionalInformationBuilder withNotifier(CustomerNotifier notifier);

	/**
	 * Method to build client
	 *
	 * @return instance of client built with given parameters
	 */
	Customer build();
}
