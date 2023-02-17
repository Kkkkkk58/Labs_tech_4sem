package ru.kslacker.banks.bankaccounts.accounttypes.api;

import ru.kslacker.banks.models.MoneyAmount;

public interface SuspiciousLimitingAccountType extends AccountType {

	MoneyAmount getSuspiciousAccountsOperationsLimit();
	void setSuspiciousAccountsOperationsLimit(MoneyAmount limit);
}
