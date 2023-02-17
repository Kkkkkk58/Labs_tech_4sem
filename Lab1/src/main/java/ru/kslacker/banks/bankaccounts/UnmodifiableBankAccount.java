package ru.kslacker.banks.bankaccounts;

import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.entities.api.Customer;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public interface UnmodifiableBankAccount {

	UUID getId();
	AccountType getType();
	Customer getHolder();
	MoneyAmount getBalance();
	MoneyAmount getDebt();
	LocalDateTime getCreationDate();
	MoneyAmount getInitialBalance();
	boolean isSuspicious();
	Collection<ReadOnlyOperationInformation> getOperationHistory();
}
