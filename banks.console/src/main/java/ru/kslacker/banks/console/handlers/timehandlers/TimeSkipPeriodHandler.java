package ru.kslacker.banks.console.handlers.timehandlers;

import ru.kslacker.banks.console.handlers.api.HandlerImpl;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Period;

public class TimeSkipPeriodHandler extends HandlerImpl {

	private final FastForwardingClock clock;
	private final BufferedWriter writer;

	public TimeSkipPeriodHandler(FastForwardingClock clock, BufferedWriter writer) {
		super("period");
		this.clock = clock;
		this.writer = writer;
	}


	@Override
	protected void handleImpl(String... args) throws IOException {
		Period period = Period.parse(args[1]);
		clock.skip(period);

		writer.write("Current time is " + clock.instant());
	}
}
