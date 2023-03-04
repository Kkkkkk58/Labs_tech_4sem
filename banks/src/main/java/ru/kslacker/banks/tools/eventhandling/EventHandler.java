package ru.kslacker.banks.tools.eventhandling;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class EventHandler<TEventArgs extends EventArgs> {

	private final Set<BiConsumer<Object, TEventArgs>> listeners = new HashSet<>();

	/**
	 * Method to add update listener
	 *
	 * @param listener update handler
	 */
	public void addListener(BiConsumer<Object, TEventArgs> listener) {
		listeners.add(listener);
	}

	/**
	 * Method to invoke event handling
	 *
	 * @param sender event sender
	 * @param args   event arguments
	 */
	public void invoke(Object sender, TEventArgs args) {
		listeners.forEach(listener -> listener.accept(sender, args));
	}
}
