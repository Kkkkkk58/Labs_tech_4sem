package ru.kslacker.banks.tools.clock;

import java.time.Clock;
import java.time.Period;


public abstract class FastForwardingClock extends Clock {

	/**
	 * Method to skip period of time
	 *
	 * @param period period of time to skip
	 */
	public abstract void skip(Period period);

	/**
	 * Method to skip given amount of days
	 *
	 * @param days number of days to skip
	 */
	public void skipDays(int days) {
		skip(Period.ofDays(days));
	}

	/**
	 * Method to skip given amount of weeks
	 *
	 * @param weeks number of weeks to skip
	 */
	public void skipWeeks(int weeks) {
		skip(Period.ofWeeks(weeks));
	}

	/**
	 * Method to skip given amount of months
	 *
	 * @param months number of months to skip
	 */
	public void skipMonths(int months) {
		skip(Period.ofMonths(months));
	}

	/**
	 * Method to skip given amount of years
	 *
	 * @param years number of years to skip
	 */
	public void skipYears(int years) {
		skip(Period.ofYears(years));
	}
}
