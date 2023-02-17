package ru.kslacker.banks.bankaccounts.accounttypes.api;

import java.math.BigDecimal;
import java.time.Period;

import ru.kslacker.banks.models.MoneyAmount;

public interface InterestCalculationAccountType extends AccountType {

	Period getInterestCalculationPeriod();
	void setInterestCalculationPeriod(Period interestCalculationPeriod);
	BigDecimal getInterestPercent(MoneyAmount initialBalance);
}
