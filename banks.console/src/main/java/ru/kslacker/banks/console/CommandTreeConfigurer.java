package ru.kslacker.banks.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import ru.kslacker.banks.console.handlers.MainHandler;
import ru.kslacker.banks.console.handlers.accounthandlers.AccountCreateCreditHandler;
import ru.kslacker.banks.console.handlers.accounthandlers.AccountCreateDebitHandler;
import ru.kslacker.banks.console.handlers.accounthandlers.AccountCreateDepositHandler;
import ru.kslacker.banks.console.handlers.accounthandlers.AccountCreateHandler;
import ru.kslacker.banks.console.handlers.accounthandlers.AccountDisplayHandler;
import ru.kslacker.banks.console.handlers.accounthandlers.AccountHandler;
import ru.kslacker.banks.console.handlers.api.Handler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankChangeHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankChangeSuspiciousOperationsLimitHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankCreateHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankDisplayHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeCreditChangeChargeHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeCreditChangeDebtLimitHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeCreditChangeHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeCreditCreateHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeCreditHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDebitChangeHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDebitChangeInterestCalculationPeriodHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDebitChangeInterestHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDebitCreateHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDebitHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDepositChangeHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDepositChangeInterestCalculationPeriodHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDepositChangeTermHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDepositCreateHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeDepositHandler;
import ru.kslacker.banks.console.handlers.bankhandlers.BankTypeHandler;
import ru.kslacker.banks.console.handlers.customerhandlers.CustomerCreateHandler;
import ru.kslacker.banks.console.handlers.customerhandlers.CustomerHandler;
import ru.kslacker.banks.console.handlers.customerhandlers.CustomerInformationAccountsHandler;
import ru.kslacker.banks.console.handlers.customerhandlers.CustomerInformationAddressSetterHandler;
import ru.kslacker.banks.console.handlers.customerhandlers.CustomerInformationDisplayHandler;
import ru.kslacker.banks.console.handlers.customerhandlers.CustomerInformationHandler;
import ru.kslacker.banks.console.handlers.customerhandlers.CustomerInformationPassportDataSetterHandler;
import ru.kslacker.banks.console.handlers.customerhandlers.CustomerInformationSetterHandler;
import ru.kslacker.banks.console.handlers.operationshandlers.AccountOperationHistoryHandler;
import ru.kslacker.banks.console.handlers.operationshandlers.OperationCancellationHandler;
import ru.kslacker.banks.console.handlers.operationshandlers.OperationDisplayHandler;
import ru.kslacker.banks.console.handlers.operationshandlers.OperationHandler;
import ru.kslacker.banks.console.handlers.operationshandlers.OperationHistoryHandler;
import ru.kslacker.banks.console.handlers.operationshandlers.OverallOperationHistoryHandler;
import ru.kslacker.banks.console.handlers.operationshandlers.ReplenishmentOperationHandler;
import ru.kslacker.banks.console.handlers.operationshandlers.TransferOperationHandler;
import ru.kslacker.banks.console.handlers.operationshandlers.WithdrawalOperationHandler;
import ru.kslacker.banks.console.handlers.timehandlers.TimeHandler;
import ru.kslacker.banks.console.handlers.timehandlers.TimeSkipDaysHandler;
import ru.kslacker.banks.console.handlers.timehandlers.TimeSkipHandler;
import ru.kslacker.banks.console.handlers.timehandlers.TimeSkipMonthsHandler;
import ru.kslacker.banks.console.handlers.timehandlers.TimeSkipPeriodHandler;
import ru.kslacker.banks.console.handlers.timehandlers.TimeSkipYearsHandler;
import ru.kslacker.banks.eventargs.DateChangedEventArgs;
import ru.kslacker.banks.models.AccountFactory;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.clock.FastForwardingClock;
import ru.kslacker.banks.tools.eventhandling.Subscribable;

public class CommandTreeConfigurer {

	private final CentralBank centralBank;
	private final FastForwardingClock clock;
	private final Subscribable<DateChangedEventArgs> updater;
	private final AccountFactory accountFactory;
	private final BufferedReader reader;
	private final BufferedWriter writer;

	public CommandTreeConfigurer(
		CentralBank centralBank,
		FastForwardingClock clock,
		Subscribable<DateChangedEventArgs> updater,
		AccountFactory accountFactory,
		BufferedReader reader,
		BufferedWriter writer)
	{
		this.centralBank = centralBank;
		this.clock = clock;
		this.updater = updater;
		this.accountFactory = accountFactory;
		this.reader = reader;
		this.writer = writer;
	}

	public Handler configure()
	{
		MainHandler mainHandler = new MainHandler();
		TimeHandler timeHandler = configureTimeHandler();
		OperationHandler operationHandler = configureOperationHandler();
		CustomerHandler customerHandler = configureCustomerHandler();
		BankHandler bankHandler = configureBankHandler();
		AccountHandler accountHandler = configureAccountHandler();

		return mainHandler
			.addSubHandler(timeHandler)
			.addSubHandler(operationHandler)
			.addSubHandler(customerHandler)
			.addSubHandler(bankHandler)
			.addSubHandler(accountHandler);
	}

	private AccountHandler configureAccountHandler()
	{
		AccountHandler accountHandler = new AccountHandler();
		AccountDisplayHandler accountDisplayHandler = new AccountDisplayHandler(
			centralBank, writer, reader);
		AccountCreateHandler accountCreateHandler = configureAccountCreateHandler();

		accountHandler
			.addSubHandler(accountDisplayHandler)
			.addSubHandler(accountCreateHandler);
		return accountHandler;
	}

	private AccountCreateHandler configureAccountCreateHandler()
	{
		AccountCreateHandler accountCreateHandler = new AccountCreateHandler();
		accountCreateHandler
			.addSubHandler(new AccountCreateCreditHandler(centralBank, writer, reader))
			.addSubHandler(new AccountCreateDebitHandler(centralBank, writer, reader))
			.addSubHandler(new AccountCreateDepositHandler(centralBank, writer, reader));

		return accountCreateHandler;
	}

	private BankHandler configureBankHandler()
	{
		BankHandler bankHandler = new BankHandler();
		BankChangeHandler bankChangeHandler = configureBankChangeHandler();
		BankCreateHandler bankCreateHandler = configureBankCreateHandler();
		BankTypeHandler bankTypeHandler = configureBankTypeHandler();
		BankDisplayHandler bankDisplayHandler = configureBankDisplayHandler();

		bankHandler
			.addSubHandler(bankTypeHandler)
			.addSubHandler(bankChangeHandler)
			.addSubHandler(bankCreateHandler)
			.addSubHandler(bankDisplayHandler);

		return bankHandler;
	}

	private BankDisplayHandler configureBankDisplayHandler()
	{
		return new BankDisplayHandler(centralBank, reader, writer);
	}

	private BankChangeHandler configureBankChangeHandler()
	{
		BankChangeHandler bankChangeHandler = new BankChangeHandler();
		bankChangeHandler
			.addSubHandler(new BankChangeSuspiciousOperationsLimitHandler(centralBank, reader, writer));

		return bankChangeHandler;
	}

	private BankCreateHandler configureBankCreateHandler()
	{
		return new BankCreateHandler(centralBank, accountFactory, clock, reader, writer);
	}

	private BankTypeHandler configureBankTypeHandler()
	{
		BankTypeHandler bankTypeHandler = new BankTypeHandler();
		BankTypeDepositHandler bankTypeDepositHandler = configureBankTypeDepositHandler();
		BankTypeDebitHandler bankTypeDebitHandler = configureBankTypeDebitHandler();
		BankTypeCreditHandler bankTypeCreditHandler = configureBankTypeCreditHandler();

		bankTypeHandler
			.addSubHandler(bankTypeDepositHandler)
			.addSubHandler(bankTypeDebitHandler)
			.addSubHandler(bankTypeCreditHandler);

		return bankTypeHandler;
	}

	private BankTypeDepositHandler configureBankTypeDepositHandler()
	{
		BankTypeDepositHandler bankTypeDepositHandler = new BankTypeDepositHandler();
		BankTypeDepositCreateHandler bankTypeDepositCreateHandler = configureBankTypeDepositCreateHandler();
		BankTypeDepositChangeHandler bankTypeDepositChangeHandler = configureBankTypeDepositChangeHandler();

		bankTypeDepositHandler
			.addSubHandler(bankTypeDepositCreateHandler)
			.addSubHandler(bankTypeDepositChangeHandler);
		return bankTypeDepositHandler;
	}

	private BankTypeDepositChangeHandler configureBankTypeDepositChangeHandler()
	{
		BankTypeDepositChangeHandler bankTypeDepositChangeHandler = new BankTypeDepositChangeHandler();
		bankTypeDepositChangeHandler
			.addSubHandler(new BankTypeDepositChangeInterestCalculationPeriodHandler(
				centralBank, reader, writer))
			.addSubHandler(new BankTypeDepositChangeTermHandler(centralBank, reader, writer));

		return bankTypeDepositChangeHandler;
	}

	private BankTypeDepositCreateHandler configureBankTypeDepositCreateHandler()
	{
		return new BankTypeDepositCreateHandler(centralBank, reader, writer);
	}

	private BankTypeDebitHandler configureBankTypeDebitHandler()
	{
		var bankTypeDebitHandler = new BankTypeDebitHandler();
		BankTypeDebitCreateHandler bankTypeDebitCreateHandler = configureBankTypeDebitCreateHandler();
		BankTypeDebitChangeHandler bankTypeDebitChangeHandler = configureBankTypeDebitChangeHandler();

		bankTypeDebitHandler
			.addSubHandler(bankTypeDebitCreateHandler)
			.addSubHandler(bankTypeDebitChangeHandler);
		return bankTypeDebitHandler;
	}

	private BankTypeDebitCreateHandler configureBankTypeDebitCreateHandler()
	{
		return new BankTypeDebitCreateHandler(centralBank, reader, writer);
	}

	private BankTypeDebitChangeHandler configureBankTypeDebitChangeHandler()
	{
		BankTypeDebitChangeHandler bankTypeDebitChangeHandler = new BankTypeDebitChangeHandler();
		bankTypeDebitChangeHandler
			.addSubHandler(new BankTypeDebitChangeInterestHandler(centralBank, reader, writer))
			.addSubHandler(new BankTypeDebitChangeInterestCalculationPeriodHandler(
				centralBank, reader, writer));

		return bankTypeDebitChangeHandler;
	}

	private BankTypeCreditHandler configureBankTypeCreditHandler()
	{
		BankTypeCreditHandler bankTypeCreditHandler = new BankTypeCreditHandler();
		BankTypeCreditCreateHandler bankTypeCreditCreateHandler = configureBankTypeCreditCreateHandler();
		BankTypeCreditChangeHandler bankTypeCreditChangeHandler = configureBankTypeCreditChangeHandler();

		bankTypeCreditHandler
			.addSubHandler(bankTypeCreditCreateHandler)
			.addSubHandler(bankTypeCreditChangeHandler);
		return bankTypeCreditHandler;
	}

	private BankTypeCreditCreateHandler configureBankTypeCreditCreateHandler()
	{
		return new BankTypeCreditCreateHandler(centralBank, reader, writer);
	}

	private BankTypeCreditChangeHandler configureBankTypeCreditChangeHandler()
	{
		var bankTypeCreditChangeHandler = new BankTypeCreditChangeHandler();
		bankTypeCreditChangeHandler
			.addSubHandler(new BankTypeCreditChangeChargeHandler(centralBank, reader, writer))
			.addSubHandler(new BankTypeCreditChangeDebtLimitHandler(centralBank, reader, writer));
		return bankTypeCreditChangeHandler;
	}

	private CustomerHandler configureCustomerHandler()
	{
		CustomerHandler customerHandler = new CustomerHandler();
		CustomerInformationHandler customerInformationHandler = configureCustomerInformationHandler();
		CustomerCreateHandler customerCreateHandler = configureCustomerCreateHandler();

		customerHandler
			.addSubHandler(customerInformationHandler)
			.addSubHandler(customerCreateHandler);

		return customerHandler;
	}

	private CustomerInformationHandler configureCustomerInformationHandler()
	{
		CustomerInformationHandler customerInformationHandler = new CustomerInformationHandler();
		CustomerInformationDisplayHandler displayHandler = configureCustomerInformationDisplayHandler();
		CustomerInformationSetterHandler setterHandler = configureCustomerInformationSetterHandler();
		CustomerInformationAccountsHandler accountsHandler = configureCustomerInformationAccountsHandler();

		customerInformationHandler
			.addSubHandler(displayHandler)
			.addSubHandler(setterHandler)
			.addSubHandler(accountsHandler);
		return customerInformationHandler;
	}

	private CustomerInformationSetterHandler configureCustomerInformationSetterHandler()
	{
		CustomerInformationSetterHandler customerInformationSetterHandler = new CustomerInformationSetterHandler();
		customerInformationSetterHandler
			.addSubHandler(new CustomerInformationAddressSetterHandler(centralBank, reader, writer))
			.addSubHandler(new CustomerInformationPassportDataSetterHandler(centralBank, reader, writer));

		return customerInformationSetterHandler;
	}

	private CustomerInformationAccountsHandler configureCustomerInformationAccountsHandler()
	{
		return new CustomerInformationAccountsHandler(centralBank, reader, writer);
	}

	private CustomerInformationDisplayHandler configureCustomerInformationDisplayHandler()
	{
		return new CustomerInformationDisplayHandler(centralBank, reader, writer);
	}

	private CustomerCreateHandler configureCustomerCreateHandler()
	{
		return new CustomerCreateHandler(centralBank, reader, writer);
	}

	private OperationHandler configureOperationHandler()
	{
		OperationHandler operationHandler = new OperationHandler();
		OperationHistoryHandler operationHistoryHandler = configureOperationHistoryHandler();

		operationHandler
			.addSubHandler(new OperationCancellationHandler(centralBank, reader, writer))
			.addSubHandler(new OperationDisplayHandler(centralBank, reader, writer))
			.addSubHandler(new ReplenishmentOperationHandler(centralBank, reader, writer))
			.addSubHandler(new TransferOperationHandler(centralBank, reader, writer))
			.addSubHandler(new WithdrawalOperationHandler(centralBank, reader, writer))
			.addSubHandler(operationHistoryHandler);

		return operationHandler;
	}

	private OperationHistoryHandler configureOperationHistoryHandler()
	{
		OperationHistoryHandler operationHistoryHandler = new OperationHistoryHandler();
		operationHistoryHandler
			.addSubHandler(new AccountOperationHistoryHandler(centralBank, reader, writer))
			.addSubHandler(new OverallOperationHistoryHandler(centralBank, writer));

		return operationHistoryHandler;
	}

	private TimeHandler configureTimeHandler()
	{
		TimeHandler timeHandler = new TimeHandler();
		TimeSkipHandler timeSkipHandler = configureTimeSkipHandler();

		timeHandler.addSubHandler(timeSkipHandler);
		return timeHandler;
	}

	private TimeSkipHandler configureTimeSkipHandler()
	{
		var timeSkipHandler = new TimeSkipHandler();
		timeSkipHandler
			.addSubHandler(new TimeSkipDaysHandler(clock, writer))
			.addSubHandler(new TimeSkipMonthsHandler(clock, writer))
			.addSubHandler(new TimeSkipPeriodHandler(clock, writer))
			.addSubHandler(new TimeSkipYearsHandler(clock, writer));
		return timeSkipHandler;
	}


}
