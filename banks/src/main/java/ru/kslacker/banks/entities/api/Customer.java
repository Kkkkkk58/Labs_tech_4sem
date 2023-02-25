package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.eventargs.CustomerAccountChangesEventArgs;
import ru.kslacker.banks.models.Address;
import ru.kslacker.banks.models.PassportData;

public interface Customer extends Subscriber<CustomerAccountChangesEventArgs> {

	/**
	 * Method to get client's first name
	 *
	 * @return client's first name
	 */
	String getFirstName();

	/**
	 * Method to get client's last name
	 *
	 * @return client's last name
	 */
	String getLastName();

	/**
	 * Method to check whether client is verified or not
	 *
	 * @return true if client is verified, otherwise - false
	 */
	boolean isVerified();

	/**
	 * Method to set client's address
	 *
	 * @param address client's address
	 */
	void setAddress(Address address);

	/**
	 * Method to set client's passport data
	 *
	 * @param passportData client's passport data
	 */
	void setPassportData(PassportData passportData);

	/**
	 * Method to set client notifier
	 *
	 * @param notifier client notifier
	 */
	void setNotifier(CustomerNotifier notifier);

	/**
	 * Method to add client notifier
	 *
	 * @param notifierDecoratorBuilder builder to add new decorator over client notifier
	 */
	void addNotifier(CustomerNotifierDecoratorBuilder notifierDecoratorBuilder);
}
