package ru.kslacker.banks.bankaccounts.accounttypes.api;

import ru.kslacker.banks.models.MoneyAmount;

public interface ChargeableAccountType {

	MoneyAmount getCharge();
	void setCharge(MoneyAmount charge);
}
