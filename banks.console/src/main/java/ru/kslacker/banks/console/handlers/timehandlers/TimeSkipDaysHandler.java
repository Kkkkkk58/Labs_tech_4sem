package ru.kslacker.banks.console.handlers.timehandlers;

import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import java.io.BufferedWriter;
import java.io.IOException;

public class TimeSkipDaysHandler extends HandlerImpl {

	private final FastForwardingClock clock;
	private final BufferedWriter writer;

	public TimeSkipDaysHandler(FastForwardingClock clock, BufferedWriter writer) {
		super("days");
		this.clock = clock;
		this.writer = writer;
	}


	@Override
	protected void handleImpl(String... args) throws IOException {
		int days = Integer.parseInt(args[1]);
		clock.skipDays(days);

		writer.write("Current time is " + clock.instant());
	}
}
