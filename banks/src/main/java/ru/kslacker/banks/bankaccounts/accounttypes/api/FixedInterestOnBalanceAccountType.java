package ru.kslacker.banks.bankaccounts.accounttypes.api;

import java.math.BigDecimal;

public interface FixedInterestOnBalanceAccountType extends InterestCalculationAccountType {

	void setInterestPercent(BigDecimal interestPercent);
}
