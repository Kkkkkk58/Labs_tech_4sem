package ru.kslacker.banks.bankaccounts.accounttypes.api;

import ru.kslacker.banks.models.MoneyAmount;

public interface DebtLimitedAccountType {

	MoneyAmount getDebtLimit();
	void setDebtLimit(MoneyAmount debtLimit);
}
