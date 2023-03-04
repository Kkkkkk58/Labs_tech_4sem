package ru.kslacker.banks.console.handlers.timehandlers;

import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import java.io.BufferedWriter;
import java.io.IOException;

public class TimeSkipYearsHandler extends HandlerImpl {

	private final FastForwardingClock clock;
	private final BufferedWriter writer;

	public TimeSkipYearsHandler(FastForwardingClock clock, BufferedWriter writer) {
		super("years");
		this.clock = clock;
		this.writer = writer;
	}


	@Override
	protected void handleImpl(String... args) throws IOException {
		int years = Integer.parseInt(args[1]);
		clock.skipYears(years);

		writer.write("Current time is " + clock.instant());
	}
}
