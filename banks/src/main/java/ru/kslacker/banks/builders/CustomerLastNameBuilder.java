package ru.kslacker.banks.builders;

public interface CustomerLastNameBuilder {

	/**
	 * Method to set client's last name
	 *
	 * @param lastName client's last name
	 * @return next state of builder to set optional information
	 */
	CustomerOptionalInformationBuilder withLastName(String lastName);
}
