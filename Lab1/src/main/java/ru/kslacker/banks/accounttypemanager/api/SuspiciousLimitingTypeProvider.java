package ru.kslacker.banks.accounttypemanager.api;

import ru.kslacker.banks.models.MoneyAmount;

public interface SuspiciousLimitingTypeProvider {

	MoneyAmount getSuspiciousAccountsOperationsLimit();
	void setSuspiciousOperationsLimit(MoneyAmount limit);
}
