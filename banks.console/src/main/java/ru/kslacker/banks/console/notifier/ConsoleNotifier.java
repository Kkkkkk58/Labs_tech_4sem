package ru.kslacker.banks.console.notifier;

import ru.kslacker.banks.entities.api.CustomerNotifier;
import ru.kslacker.banks.models.Message;

public class ConsoleNotifier implements CustomerNotifier {

	@Override
	public void send(Message message) {
		System.out.println(message);
	}
}
