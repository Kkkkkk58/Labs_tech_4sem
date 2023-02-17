package ru.kslacker.banks.tools.clock;

import java.time.Clock;
import java.time.Period;


public abstract class FastForwardingClock extends Clock {

	public abstract void skip(Period period);

	public void skipDays(int days) {
		skip(Period.ofDays(days));
	}

	public void skipWeeks(int weeks) {
		skip(Period.ofWeeks(weeks));
	}

	public void skipMonths(int months) {
		skip(Period.ofMonths(months));
	}

	public void skipYears(int years) {
		skip(Period.ofYears(years));
	}
}
