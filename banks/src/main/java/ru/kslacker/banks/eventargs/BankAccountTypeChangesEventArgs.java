package ru.kslacker.banks.eventargs;

import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.tools.eventhandling.EventArgs;


public record BankAccountTypeChangesEventArgs(AccountType accountType, String updateInfo)
	implements EventArgs {

}
