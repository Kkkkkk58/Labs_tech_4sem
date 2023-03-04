package ru.kslacker.banks.accounttypemanager.api;

import java.util.UUID;
import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;

public interface AccountTypeManager extends
	DebitTypeProvider,
	CreditTypeProvider,
	DepositTypeProvider {

	/**
	 * Return account type by id
	 *
	 * @param id account type id
	 * @return account type
	 */
	AccountType getAccountType(UUID id);
}
