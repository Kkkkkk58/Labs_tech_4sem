package ru.kslacker.banks.accounttypemanager.api;

import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import java.util.UUID;

public interface AccountTypeManager extends DebitTypeProvider, CreditTypeProvider, DepositTypeProvider {

	AccountType getAccountType(UUID id);
}
