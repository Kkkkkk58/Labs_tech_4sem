package ru.kslacker.banks.entities.api;

import java.util.UUID;

public interface Subscriber<TEventArgs> {

	/**
	 * Method to get id of entity
	 *
	 * @return id
	 */
	UUID getId();

	/**
	 * Method to update on events
	 *
	 * @param sender    event sender
	 * @param eventArgs event arguments
	 */
	void update(Object sender, TEventArgs eventArgs);
}
