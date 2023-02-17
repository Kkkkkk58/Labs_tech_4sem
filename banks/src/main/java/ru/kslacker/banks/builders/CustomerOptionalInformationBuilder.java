package ru.kslacker.banks.builders;

import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.entities.api.CustomerNotifier;
import ru.kslacker.banks.models.Address;
import ru.kslacker.banks.models.PassportData;

public interface CustomerOptionalInformationBuilder {

	CustomerOptionalInformationBuilder withAddress(Address address);
	CustomerOptionalInformationBuilder withPassportData(PassportData passportData);
	CustomerOptionalInformationBuilder withNotifier(CustomerNotifier notifier);

	Customer build();
}
