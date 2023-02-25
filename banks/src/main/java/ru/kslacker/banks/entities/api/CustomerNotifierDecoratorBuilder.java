package ru.kslacker.banks.entities.api;

public interface CustomerNotifierDecoratorBuilder {

	/**
	 * Method to add wrapped customer notifier
	 *
	 * @param wrapped wrapped customer notifier
	 * @return instance of customer notifier builder
	 */
	CustomerNotifierBuilder withWrapped(CustomerNotifier wrapped);
}
