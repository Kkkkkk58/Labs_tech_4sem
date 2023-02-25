package ru.kslacker.banks.bankaccounts.accounttypes.api;

public interface CreditAccountType extends
	ChargeableAccountType,
	DebtLimitedAccountType,
	SuspiciousLimitingAccountType {

}
