package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.models.Message;

public interface CustomerNotifier {

	/**
	 * Method to send notification to customer
	 *
	 * @param message notification message
	 */
	void send(Message message);
}
