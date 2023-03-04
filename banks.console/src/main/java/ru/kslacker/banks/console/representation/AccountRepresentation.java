package ru.kslacker.banks.console.representation;

import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.models.MoneyAmount;
import java.time.LocalDateTime;
import java.util.UUID;

public class AccountRepresentation {

	private final UUID accountId;
	private final UUID holderId;
	private final MoneyAmount balance;
	private final MoneyAmount debt;
	private final LocalDateTime creationDate;

	public AccountRepresentation(UnmodifiableBankAccount account) {
		this.accountId = account.getId();
		this.holderId = account.getHolder().getId();
		this.balance = account.getBalance();
		this.debt = account.getDebt();
		this.creationDate = account.getCreationDate();
	}

	@Override
	public String toString() {
		return
			"Bank account [" + accountId + "]\n" +
			"Holder: [" + holderId + "]\n" +
			"Balance: " + balance + "\n" +
			"Debt: " + debt + "\n" +
			"Creation date: " + creationDate;
	}
}
