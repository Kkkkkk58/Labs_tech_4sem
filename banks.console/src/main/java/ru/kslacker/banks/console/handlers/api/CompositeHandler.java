package ru.kslacker.banks.console.handlers.api;

public interface CompositeHandler extends Handler {
	CompositeHandler addSubHandler(Handler handler);
}
