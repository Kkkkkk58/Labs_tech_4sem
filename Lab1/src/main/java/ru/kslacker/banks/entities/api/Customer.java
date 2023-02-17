package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.eventargs.CustomerAccountChangesEventArgs;
import ru.kslacker.banks.models.Address;
import ru.kslacker.banks.models.PassportData;

public interface Customer extends Subscriber<CustomerAccountChangesEventArgs> {

	String getFirstName();
	String getLastName();
	boolean isVerified();

	void setAddress(Address address);
	void setPassportData(PassportData passportData);
	void setNotifier(CustomerNotifier notifier);
	void addNotifier(CustomerNotifierDecoratorBuilder notifierDecoratorBuilder);
}
