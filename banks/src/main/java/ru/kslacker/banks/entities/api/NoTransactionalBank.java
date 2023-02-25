package ru.kslacker.banks.entities.api;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import ru.kslacker.banks.accounttypemanager.api.AccountTypeManager;
import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.models.MoneyAmount;

public interface NoTransactionalBank extends
	CreditAccountProvider,
	DebitAccountProvider,
	DepositAccountProvider {

	/**
	 * Method to get id of the bank
	 *
	 * @return bank id
	 */
	UUID getId();

	/**
	 * Method to get name of the bank
	 *
	 * @return bank name
	 */
	String getName();

	/**
	 * Method to get the limit on operations with suspicious accounts
	 *
	 * @return limit on operations with suspicious accounts
	 */
	MoneyAmount getSuspiciousAccountsOperationsLimit();

	/**
	 * Method to get account type manager
	 * @return AccountTypeManager instance
	 */
	AccountTypeManager getAccountTypeManager();

	/**
	 * Method to get list of customers
	 * @return collection of registered customers
	 */
	Collection<Customer> getCustomers();

	/**
	 * Method to register new customer
	 * @param customer customer
	 * @return registered customer
	 */
	Customer registerCustomer(Customer customer);

	/**
	 * Method to set the limit on operations with suspicious accounts
	 * @param limit new limit on operations with suspicious accounts
	 */
	void setSuspiciousAccountsOperationsLimit(MoneyAmount limit);

	/**
	 * Method to get accounts of given customer
	 * @param accountHolderId customer id
	 * @return collection of information about accounts of users
	 */
	Collection<UnmodifiableBankAccount> getAccounts(UUID accountHolderId);

	/**
	 * Method to find account by id
	 * @param accountId account id
	 * @return optional information about account
	 */
	Optional<UnmodifiableBankAccount> findAccount(UUID accountId);

	/**
	 * Method to get account by id
	 * @param accountId account id
	 * @return information about account
	 */
	UnmodifiableBankAccount getAccount(UUID accountId);

}
