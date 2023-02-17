package ru.kslacker.banks.accounttypemanager.api;


import java.time.Period;
import java.util.UUID;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.models.InterestOnBalancePolicy;

public interface DepositTypeProvider extends SuspiciousLimitingTypeProvider, InterestCalculatingTypeProvider {

	DepositAccountType createDepositAccountType(Period depositTerm, InterestOnBalancePolicy interestOnBalancePolicy, Period interestCalculationPeriod);
	void setInterestOnBalancePolicy(UUID depositTypeId, InterestOnBalancePolicy policy);
	void setDepositTerm(UUID depositTypeId, Period depositTerm);
}
