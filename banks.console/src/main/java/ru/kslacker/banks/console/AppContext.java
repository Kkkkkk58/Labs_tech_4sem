package ru.kslacker.banks.console;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.kslacker.banks.eventargs.DateChangedEventArgs;
import ru.kslacker.banks.models.AccountFactory;
import ru.kslacker.banks.models.AccountFactoryImpl;
import ru.kslacker.banks.services.CentralBankImpl;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import ru.kslacker.banks.tools.clock.FastForwardingSubscribableClock;
import ru.kslacker.banks.tools.eventhandling.Subscribable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
@ComponentScan("ru.kslacker.banks.console")
public class AppContext {

	@Bean
	public CentralBank centralBank() {
		return new CentralBankImpl(clock());
	}

	@Bean
	public FastForwardingClock clock() {
		return ffSubscribableClock();
	}

	@Bean
	public Subscribable<DateChangedEventArgs> updater() {
		return ffSubscribableClock();
	}

	@Bean
	public AccountFactory accountFactory() {
		return new AccountFactoryImpl(clock(), updater());
	}

	@Bean
	public BufferedReader reader() {
		return new BufferedReader(new InputStreamReader(System.in));
	}

	@Bean
	public BufferedWriter writer() {
		return new BufferedWriter(new OutputStreamWriter(System.out));
	}

	private FastForwardingSubscribableClock ffSubscribableClock() {
		return new FastForwardingSubscribableClock(LocalDateTime.now(), ZoneId.systemDefault());
	}
}
