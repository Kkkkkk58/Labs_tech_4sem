package ru.kslacker.banks.console.handlers.timehandlers;

import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import java.io.BufferedWriter;
import java.io.IOException;

public class TimeSkipWeeksHandler extends HandlerImpl {

	private final FastForwardingClock clock;
	private final BufferedWriter writer;

	public TimeSkipWeeksHandler(FastForwardingClock clock, BufferedWriter writer) {
		super("weeks");
		this.clock = clock;
		this.writer = writer;
	}


	@Override
	protected void handleImpl(String... args) throws IOException {
		int weeks = Integer.parseInt(args[1]);
		clock.skipWeeks(weeks);

		writer.write("Current time is " + clock.instant());
	}
}
