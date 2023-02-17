package ru.kslacker.banks.tools.eventhandling;

import java.util.function.BiConsumer;

public interface Subscribable<TEventArgs extends EventArgs> {
	void subscribe(BiConsumer<Object, TEventArgs> update);
}
