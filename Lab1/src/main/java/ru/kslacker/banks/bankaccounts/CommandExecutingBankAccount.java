package ru.kslacker.banks.bankaccounts;

import ru.kslacker.banks.commands.Command;
import ru.kslacker.banks.transactions.Transaction;

public interface CommandExecutingBankAccount extends UnmodifiableBankAccount {

	void execute(Command command, Transaction transaction);
}
