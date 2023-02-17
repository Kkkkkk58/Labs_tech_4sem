package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.accounttypemanager.api.AccountTypeManager;
import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.models.MoneyAmount;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface NoTransactionalBank extends CreditAccountProvider, DebitAccountProvider, DepositAccountProvider {

	UUID getId();
	String getName();
	MoneyAmount getSuspiciousAccountsOperationsLimit();
	AccountTypeManager getAccountTypeManager();
	Collection<Customer> getCustomers();

	Customer registerCustomer(Customer customer);
	void setSuspiciousAccountsOperationsLimit(MoneyAmount limit);
	Collection<UnmodifiableBankAccount> getAccounts(UUID accountHolderId);
	Optional<UnmodifiableBankAccount> findAccount(UUID accountId);
	UnmodifiableBankAccount getAccount(UUID accountId);


}
