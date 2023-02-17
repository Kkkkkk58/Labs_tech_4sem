package ru.kslacker.banks.accounttypemanager.api;

import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.models.MoneyAmount;
import java.util.UUID;

public interface CreditTypeProvider extends SuspiciousLimitingTypeProvider {

	CreditAccountType createCreditAccountType(MoneyAmount debtLimit, MoneyAmount charge);
	void setDebtLimit(UUID creditTypeId, MoneyAmount debtLimit);
	void setCharge(UUID creditTypeId, MoneyAmount charge);
}
