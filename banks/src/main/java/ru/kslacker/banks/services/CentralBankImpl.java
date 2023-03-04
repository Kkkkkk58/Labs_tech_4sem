package ru.kslacker.banks.services;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.bankaccounts.CommandExecutingBankAccount;
import ru.kslacker.banks.commands.ReplenishmentCommand;
import ru.kslacker.banks.commands.TransferCommand;
import ru.kslacker.banks.commands.WithdrawalCommand;
import ru.kslacker.banks.entities.api.Bank;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.exceptions.CentralBankException;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.OperationInformation;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import ru.kslacker.banks.services.api.CentralBank;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import ru.kslacker.banks.transactions.Transaction;
import ru.kslacker.banks.transactions.TransactionImpl;
import ru.kslacker.banks.transactions.states.SuccessfulTransactionState;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;

@ExtensionMethod(StreamExtensions.class)
public class CentralBankImpl implements CentralBank {

	private final List<Bank> banks;
	private final Clock clock;
	private final List<Transaction> transactions;

	/**
	 * Constructor of default implementation of CentralBank
	 *
	 * @param clock clock to track current time
	 */
	public CentralBankImpl(Clock clock) {
		this.banks = new ArrayList<>();
		this.clock = clock;
		this.transactions = new ArrayList<>();
	}

	@Override
	public Collection<NoTransactionalBank> getBanks() {
		return Collections.unmodifiableCollection(banks);
	}

	@Override
	public Collection<ReadOnlyOperationInformation> getOperations() {
		return transactions
			.stream()
			.map(t -> (ReadOnlyOperationInformation) t.getInformation())
			.toList();
	}

	@Override
	public NoTransactionalBank registerBank(Bank bank) {
		if (banks.contains(bank)) {
			throw CentralBankException.bankAlreadyExists(bank.getId());
		}

		banks.add(bank);
		return bank;
	}

	@Override
	public void cancelTransaction(UUID transactionId) {
		Transaction transaction = transactions.stream()
			.single(t -> t.getInformation().getId().equals(transactionId));

		transaction.cancel();
	}

	@Override
	public ReadOnlyOperationInformation withdraw(UUID accountId, MoneyAmount moneyAmount) {
		CommandExecutingBankAccount commandExecutingBankAccount =
			getCommandExecutingBankAccount(accountId);

		OperationInformation operationInformation = new OperationInformation(
			commandExecutingBankAccount,
			moneyAmount,
			LocalDateTime.now(clock));

		TransactionImpl transaction = new TransactionImpl(
			operationInformation,
			new WithdrawalCommand());

		return performTransaction(transaction);
	}

	@Override
	public ReadOnlyOperationInformation replenish(UUID accountId, MoneyAmount moneyAmount) {
		CommandExecutingBankAccount commandExecutingBankAccount =
			getCommandExecutingBankAccount(accountId);

		OperationInformation operationInformation = new OperationInformation(
			commandExecutingBankAccount,
			moneyAmount,
			LocalDateTime.now(clock));

		TransactionImpl transaction = new TransactionImpl(
			operationInformation,
			new ReplenishmentCommand());

		return performTransaction(transaction);
	}

	@Override
	public ReadOnlyOperationInformation transfer(UUID fromAccountId, UUID toAccountId,
		MoneyAmount moneyAmount) {
		CommandExecutingBankAccount from = getCommandExecutingBankAccount(fromAccountId);
		CommandExecutingBankAccount to = getCommandExecutingBankAccount(toAccountId);

		OperationInformation operationInformation = new OperationInformation(
			from,
			moneyAmount,
			LocalDateTime.now(clock));

		TransactionImpl transaction = new TransactionImpl(
			operationInformation,
			new TransferCommand(to));

		return performTransaction(transaction);
	}

	private ReadOnlyOperationInformation performTransaction(Transaction transaction) {
		transactions.add(transaction);

		transaction.perform();
		transaction.getInformation().setCompletionTime(LocalDateTime.now(clock));
		transaction.setState(new SuccessfulTransactionState(transaction));

		return transaction.getInformation();
	}

	private CommandExecutingBankAccount getCommandExecutingBankAccount(UUID accountId) {
		return banks
			.stream()
			.single(bank -> bank.findAccount(accountId).isPresent())
			.getExecutingAccount(accountId);
	}
}
