package ru.kslacker.banks.models;

import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.entities.api.Customer;

public interface AccountFactory {

	BankAccount createDebitAccount(DebitAccountType type, Customer customer, MoneyAmount balance);
	BankAccount createDepositAccount(DepositAccountType type, Customer customer, MoneyAmount balance);
	BankAccount createCreditAccount(CreditAccountType type, Customer customer, MoneyAmount balance);
}
