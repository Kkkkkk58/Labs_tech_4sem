package ru.kslacker.banks.console.handlers.timehandlers;

import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import java.io.BufferedWriter;
import java.io.IOException;

public class TimeSkipMonthsHandler extends HandlerImpl {

	private final FastForwardingClock clock;
	private final BufferedWriter writer;

	public TimeSkipMonthsHandler(FastForwardingClock clock, BufferedWriter writer) {
		super("months");
		this.clock = clock;
		this.writer = writer;
	}


	@Override
	protected void handleImpl(String... args) throws IOException {
		int months = Integer.parseInt(args[1]);
		clock.skipMonths(months);

		writer.write("Current time is " + clock.instant());
	}
}
