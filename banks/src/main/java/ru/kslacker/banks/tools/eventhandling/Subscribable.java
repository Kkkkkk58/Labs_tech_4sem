package ru.kslacker.banks.tools.eventhandling;

import java.util.function.BiConsumer;

public interface Subscribable<TEventArgs extends EventArgs> {

	/**
	 * Method to subscribe to updates
	 * @param update function to call on updates
	 */
	void subscribe(BiConsumer<Object, TEventArgs> update);
}
