package ru.kslacker.banks.entities.api;

public interface CustomerNotifierDecoratorBuilder {

	CustomerNotifierBuilder withWrapped(CustomerNotifier wrapped);
}
