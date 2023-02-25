package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class SubscriptionException extends BanksDomainException {

	private SubscriptionException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when the subscriber with given id is already subscribed
	 *
	 * @param subscriberId subscriber's id
	 * @return exception with corresponding message
	 */
	public static SubscriptionException alreadySubscribed(UUID subscriberId) {
		return new SubscriptionException(
			"Subscriber " + subscriberId + " is already subscribed to notifications");
	}

	/**
	 * Exception thrown when subscriber with given id wasn't found
	 *
	 * @param subscriberId subscriber's id
	 * @return exception with corresponding message
	 */
	public static SubscriptionException subscriberNotFound(UUID subscriberId) {
		return new SubscriptionException("Subscriber " + subscriberId + " was not found");
	}
}
