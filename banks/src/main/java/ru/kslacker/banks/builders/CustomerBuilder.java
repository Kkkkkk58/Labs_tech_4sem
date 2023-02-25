package ru.kslacker.banks.builders;

public interface CustomerBuilder {

	/**
	 * Method to set client's first name
	 *
	 * @param firstName first name of customer
	 * @return next state of builder to set last name
	 */
	CustomerLastNameBuilder withFirstName(String firstName);
}
