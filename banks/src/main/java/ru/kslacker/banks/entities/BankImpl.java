package ru.kslacker.banks.entities;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.accounttypemanager.AccountTypeManagerImpl;
import ru.kslacker.banks.accounttypemanager.ObservableAccountTypeManager;
import ru.kslacker.banks.accounttypemanager.api.AccountTypeManager;
import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.CommandExecutingBankAccount;
import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.entities.api.Bank;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.entities.api.Subscriber;
import ru.kslacker.banks.eventargs.BankAccountTypeChangesEventArgs;
import ru.kslacker.banks.eventargs.CustomerAccountChangesEventArgs;
import ru.kslacker.banks.exceptions.BankException;
import ru.kslacker.banks.exceptions.SubscriptionException;
import ru.kslacker.banks.models.AccountFactory;
import ru.kslacker.banks.models.Message;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.tools.extensions.StreamExtensions;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ExtensionMethod(StreamExtensions.class)
public class BankImpl implements Bank {

	private final AccountFactory accountFactory;
	private final List<BankAccount> accounts;
	private final List<Subscriber<CustomerAccountChangesEventArgs>> subscribers;
	private final List<Customer> customers;
	private final Clock clock;
	@Include
	private final UUID id;
	private final String name;
	private final AccountTypeManager accountTypeManager;

	/**
	 * Constructor of default bank implementation
	 *
	 * @param name                      name of the bank
	 * @param accountFactory            factory providing accounts
	 * @param suspiciousOperationsLimit limit on operations with suspicious accounts
	 * @param clock                     clock to track time
	 */
	public BankImpl(
		String name,
		AccountFactory accountFactory,
		MoneyAmount suspiciousOperationsLimit,
		Clock clock) {

		this.id = UUID.randomUUID();
		this.name = name;
		this.accountFactory = accountFactory;
		this.accounts = new ArrayList<>();
		this.subscribers = new ArrayList<>();
		this.customers = new ArrayList<>();
		this.clock = clock;
		this.accountTypeManager = new ObservableAccountTypeManager(
			new AccountTypeManagerImpl(suspiciousOperationsLimit), this::notifySubscribers);
	}

	@Override
	public CommandExecutingBankAccount getExecutingAccount(UUID id) {
		return accounts.stream()
			.single(account -> account.getId() == id);
	}

	@Override
	public UnmodifiableBankAccount createCreditAccount(AccountType type, Customer customer,
		MoneyAmount balance) {

		if (!(type instanceof CreditAccountType creditType)) {
			throw BankException.invalidAccountTypeCreation();
		}

		BankAccount account = accountFactory.createCreditAccount(creditType, customer, balance);
		accounts.add(account);

		return account;
	}

	@Override
	public UnmodifiableBankAccount createDebitAccount(AccountType type, Customer customer,
		MoneyAmount balance) {

		if (!(type instanceof DebitAccountType debitType)) {
			throw BankException.invalidAccountTypeCreation();
		}

		BankAccount account = accountFactory.createDebitAccount(debitType, customer, balance);
		accounts.add(account);

		return account;
	}

	@Override
	public UnmodifiableBankAccount createDepositAccount(AccountType type, Customer customer,
		MoneyAmount balance) {

		if (!(type instanceof DepositAccountType depositType)) {
			throw BankException.invalidAccountTypeCreation();
		}

		BankAccount account = accountFactory.createDepositAccount(depositType, customer, balance);
		accounts.add(account);

		return account;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public MoneyAmount getSuspiciousAccountsOperationsLimit() {
		return accountTypeManager.getSuspiciousAccountsOperationsLimit();
	}

	@Override
	public AccountTypeManager getAccountTypeManager() {
		return accountTypeManager;
	}

	@Override
	public Collection<Customer> getCustomers() {
		return Collections.unmodifiableCollection(customers);
	}

	@Override
	public Customer registerCustomer(Customer customer) {
		if (customers.contains(customer)) {
			throw BankException.customerAlreadyExists(id, customer.getId());
		}

		customers.add(customer);
		subscribe(customer);

		return customer;
	}

	@Override
	public void setSuspiciousAccountsOperationsLimit(MoneyAmount limit) {
		accountTypeManager.setSuspiciousOperationsLimit(limit);
	}

	@Override
	public Collection<UnmodifiableBankAccount> getAccounts(UUID accountHolderId) {
		return accounts
			.stream()
			.filter(account -> account.getHolder().getId().equals(accountHolderId))
			.collect(Collectors.toList());
	}

	@Override
	public Optional<UnmodifiableBankAccount> findAccount(UUID accountId) {
		return accounts
			.stream()
			.filter(account -> account.getId().equals(accountId))
			.map(account -> (UnmodifiableBankAccount) account)
			.reduce((a, b) -> {
				throw new UnsupportedOperationException();
			});
	}

	@Override
	public UnmodifiableBankAccount getAccount(UUID accountId) {
		return findAccount(accountId)
			.orElseThrow(() -> BankException.accountNotFound(accountId));
	}

	// TODO Add subscriptions to interface
	public void subscribe(Subscriber<CustomerAccountChangesEventArgs> subscriber) {
		if (subscribers.contains(subscriber)) {
			throw SubscriptionException.alreadySubscribed(subscriber.getId());
		}
		if (customers.stream().noneMatch(customer -> customer.getId().equals(subscriber.getId()))) {
			throw BankException.customerNotFound(subscriber.getId());
		}

		subscribers.add(subscriber);
	}

	public void unsubscribe(Subscriber<CustomerAccountChangesEventArgs> subscriber) {
		if (!subscribers.remove(subscriber)) {
			throw SubscriptionException.subscriberNotFound(subscriber.getId());
		}
	}

	private void notifySubscribers(Object sender, BankAccountTypeChangesEventArgs eventArgs) {

		Message message = createMessage(eventArgs);
		var messageEventArgs = new CustomerAccountChangesEventArgs(message);

		for (Subscriber<CustomerAccountChangesEventArgs> subscriber : subscribers) {
			NotifySubscriberWithSuitableAccount(subscriber, messageEventArgs,
				eventArgs.accountType());
		}
	}

	private void NotifySubscriberWithSuitableAccount(
		Subscriber<CustomerAccountChangesEventArgs> subscriber,
		CustomerAccountChangesEventArgs messageEventArgs,
		AccountType accountType) {
		Customer customer = customers.stream()
			.single(c -> c.getId().equals(subscriber.getId()));

		if (getAccounts(customer.getId()).stream()
			.anyMatch(account -> account.getType().equals(accountType))) {
			subscriber.update(this, messageEventArgs);
		}
	}

	private Message createMessage(BankAccountTypeChangesEventArgs eventArgs) {
		return new Message(
			name,
			"Changes in your account type " + eventArgs.accountType().getId() + " details",
			eventArgs.updateInfo(),
			LocalDateTime.now(clock));
	}
}
