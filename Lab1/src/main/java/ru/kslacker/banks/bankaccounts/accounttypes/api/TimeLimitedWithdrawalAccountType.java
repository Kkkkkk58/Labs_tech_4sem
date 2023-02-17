package ru.kslacker.banks.bankaccounts.accounttypes.api;


import java.time.Period;

public interface TimeLimitedWithdrawalAccountType extends AccountType {

	Period getDepositTerm();
	void setDepositTerm(Period depositTerm);
}
