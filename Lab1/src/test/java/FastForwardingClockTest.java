import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import ru.kslacker.banks.tools.clock.FastForwardingSubscribableClock;

public class FastForwardingClockTest {

	@Test
	public void skipTime_ClockCurrentTimeChanged() {
		FastForwardingClock clock = new FastForwardingSubscribableClock(LocalDateTime.now(), ZoneId.systemDefault());
		LocalDateTime currentTime = LocalDateTime.now(clock);

		Period timeDifference = Period.ofDays(42);
		clock.skip(timeDifference);
		LocalDateTime newTime = LocalDateTime.now(clock);

		Assertions.assertEquals(Period.between(currentTime.toLocalDate(), newTime.toLocalDate()), timeDifference);
	}

}
