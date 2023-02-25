package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.bankaccounts.CommandExecutingBankAccount;

import java.util.UUID;

public interface Bank extends NoTransactionalBank {

	/**
	 * Method to get account that can execute commands by id
	 *
	 * @param id id of bank account
	 * @return account that can execute commands
	 */
	CommandExecutingBankAccount getExecutingAccount(UUID id);
}
