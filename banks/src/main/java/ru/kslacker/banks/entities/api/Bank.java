package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.bankaccounts.CommandExecutingBankAccount;
import java.util.UUID;

public interface Bank extends NoTransactionalBank {

	CommandExecutingBankAccount getExecutingAccount(UUID id);
}
