package ru.kslacker.banks.entities.api;

import java.util.UUID;

// TODO eventArgs research
public interface Subscriber<TEventArgs> {

	UUID getId();
	void update(Object sender, TEventArgs eventArgs);
}
