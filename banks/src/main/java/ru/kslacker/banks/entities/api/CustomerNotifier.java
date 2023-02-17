package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.models.Message;

public interface CustomerNotifier {

	void send(Message message);
}
