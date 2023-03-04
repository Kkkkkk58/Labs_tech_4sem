package ru.kslacker.banks.tools.clock;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.function.BiConsumer;
import ru.kslacker.banks.eventargs.DateChangedEventArgs;
import ru.kslacker.banks.tools.eventhandling.EventHandler;
import ru.kslacker.banks.tools.eventhandling.Subscribable;


public class FastForwardingSubscribableClock extends FastForwardingClock implements
	Subscribable<DateChangedEventArgs> {

	private LocalDateTime now;
	private final ZoneId zoneId;
	private final EventHandler<DateChangedEventArgs> eventHandler;

	/**
	 * Constructor of clock that lets skip time and subscribe to events
	 *
	 * @param curTime current time
	 * @param zoneId  time zone id
	 */
	public FastForwardingSubscribableClock(LocalDateTime curTime, ZoneId zoneId) {
		this.now = curTime;
		this.zoneId = zoneId;
		this.eventHandler = new EventHandler<>();
	}

	@Override
	public ZoneId getZone() {
		return zoneId;
	}

	@Override
	public Clock withZone(ZoneId zoneId) {
		return new FastForwardingSubscribableClock(now, zoneId);
	}

	@Override
	public Instant instant() {
		return now.toInstant(getZone().getRules().getOffset(now));
	}

	@Override
	public void subscribe(BiConsumer<Object, DateChangedEventArgs> update) {
		eventHandler.addListener(update);
	}

	@Override
	public void skip(Period period) {
		now = now.plus(period);
		notifySubscribers();
	}

	@Override
	public void skipDays(int days) {
		skip(Period.ofDays(days));
	}

	@Override
	public void skipMonths(int months) {
		skip(Period.ofMonths(months));
	}

	@Override
	public void skipWeeks(int weeks) {
		skip(Period.ofWeeks(weeks));
	}

	@Override
	public void skipYears(int years) {
		skip(Period.ofYears(years));
	}

	private void notifySubscribers() {
		eventHandler.invoke(this, new DateChangedEventArgs(now));
	}
}
