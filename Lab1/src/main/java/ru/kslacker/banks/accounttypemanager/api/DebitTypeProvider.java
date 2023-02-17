package ru.kslacker.banks.accounttypemanager.api;

import java.math.BigDecimal;

import java.time.Period;
import java.util.UUID;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;

public interface DebitTypeProvider extends SuspiciousLimitingTypeProvider, InterestCalculatingTypeProvider {

	DebitAccountType createDebitAccountType(BigDecimal interestOnBalance, Period interestCalculationPeriod);
	void setInterestOnBalance(UUID debitTypeId, BigDecimal interestOnBalance);
}
