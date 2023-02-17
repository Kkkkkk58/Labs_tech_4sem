package ru.kslacker.banks.entities.api;

import ru.kslacker.banks.bankaccounts.UnmodifiableBankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.models.MoneyAmount;

public interface CreditAccountProvider {

	UnmodifiableBankAccount createCreditAccount(AccountType type, Customer customer, MoneyAmount balance);
}
