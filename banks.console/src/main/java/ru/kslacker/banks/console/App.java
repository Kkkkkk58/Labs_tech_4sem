package ru.kslacker.banks.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kslacker.banks.console.handlers.api.Handler;
import ru.kslacker.banks.eventargs.DateChangedEventArgs;
import ru.kslacker.banks.exceptions.BanksDomainException;
import ru.kslacker.banks.models.AccountFactory;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import ru.kslacker.banks.tools.eventhandling.Subscribable;

@Component
public class App {

	private final BufferedReader reader;
	private final Handler baseHandler;

	@Autowired
	public App(
		CentralBank centralBank,
		FastForwardingClock clock,
		Subscribable<DateChangedEventArgs> updater,
		AccountFactory accountFactory,
		BufferedReader reader,
		BufferedWriter writer) {

		this.reader = reader;
		this.baseHandler = new CommandTreeConfigurer(centralBank, clock, updater, accountFactory, reader, writer).configure();
	}

	public void run()
	{
		try
		{
			while (true)
			{
				try
				{
					String[] command = reader.readLine().split(" ");
					baseHandler.handle(command);
				}
				catch (BanksDomainException e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("An error occurred during the execution. Shutting down the app...", e);
		}
	}
}
