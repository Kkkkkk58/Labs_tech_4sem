package ru.kslacker.banks.bankaccounts.accounttypes.api;

import ru.kslacker.banks.models.InterestOnBalancePolicy;

public interface InterestGradesAccountType extends InterestCalculationAccountType {

	InterestOnBalancePolicy getInterestOnBalancePolicy();
	void setInterestOnBalancePolicy(InterestOnBalancePolicy policy);
}
