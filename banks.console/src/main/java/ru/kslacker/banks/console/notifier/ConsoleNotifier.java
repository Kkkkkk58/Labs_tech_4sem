package ru.kslacker.banks.console.notifier;

import lombok.AllArgsConstructor;
import ru.kslacker.banks.console.exceptions.BanksConsoleException;
import ru.kslacker.banks.entities.api.CustomerNotifier;
import ru.kslacker.banks.models.Message;
import java.io.BufferedWriter;
import java.io.IOException;

@AllArgsConstructor
public class ConsoleNotifier implements CustomerNotifier {

	private final BufferedWriter writer;

	@Override
	public void send(Message message) {
		try {
			writer.write(message.toString());
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			throw new BanksConsoleException("Problems with notifying customer");
		}
	}
}
