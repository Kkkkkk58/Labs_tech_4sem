package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class SubscriptionException extends BanksDomainException {

	private SubscriptionException(String message) {
		super(message);
	}

	public static SubscriptionException alreadySubscribed(UUID subscriberId)
	{
		return new SubscriptionException("Subscriber " + subscriberId + " is already subscribed to notifications");
	}

	public static SubscriptionException subscriberNotFound(UUID subscriberId)
	{
		return new SubscriptionException("Subscriber " + subscriberId + " was not found");
	}
}
