package ru.kslacker.banks.services.api;

import ru.kslacker.banks.entities.api.Bank;
import ru.kslacker.banks.entities.api.NoTransactionalBank;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import java.util.Collection;
import java.util.UUID;

public interface CentralBank {

	Collection<NoTransactionalBank> getBanks();
	Collection<ReadOnlyOperationInformation> getOperations();
	NoTransactionalBank registerBank(Bank bank);
	void cancelTransaction(UUID transactionId);
	ReadOnlyOperationInformation withdraw(UUID accountId, MoneyAmount moneyAmount);
	ReadOnlyOperationInformation replenish(UUID accountId, MoneyAmount moneyAmount);
	ReadOnlyOperationInformation transfer(UUID fromAccountId, UUID toAccountId, MoneyAmount moneyAmount);
}
