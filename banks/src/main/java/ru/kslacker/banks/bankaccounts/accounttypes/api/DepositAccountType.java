package ru.kslacker.banks.bankaccounts.accounttypes.api;

public interface DepositAccountType extends
	InterestGradesAccountType,
	TimeLimitedWithdrawalAccountType,
	SuspiciousLimitingAccountType {

}
