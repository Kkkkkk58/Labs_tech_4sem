package ru.kslacker.banks.bankaccounts.accounttypes;

import java.math.BigDecimal;
import java.time.Period;

import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.SuspiciousLimitingAccountTypeBase;
import ru.kslacker.banks.models.MoneyAmount;

public class DebitAccountTypeImpl extends SuspiciousLimitingAccountTypeBase implements DebitAccountType {

	private BigDecimal interestPercent;
	private Period interestCalculationPeriod;

	public DebitAccountTypeImpl(
		BigDecimal interestPercent,
		Period interestCalculationPeriod,
		MoneyAmount suspiciousAccountsOperationsLimit) {

		super(suspiciousAccountsOperationsLimit);
		this.interestPercent = interestPercent;
		this.interestCalculationPeriod = interestCalculationPeriod;
	}

	@Override
	public void setInterestPercent(BigDecimal interestPercent) {
		if (interestPercent.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}

		this.interestPercent = interestPercent;
	}

	@Override
	public Period getInterestCalculationPeriod() {
		return interestCalculationPeriod;
	}

	@Override
	public void setInterestCalculationPeriod(Period interestCalculationPeriod) {
		this.interestCalculationPeriod = interestCalculationPeriod;
	}

	@Override
	public BigDecimal getInterestPercent(MoneyAmount initialBalance) {
		return interestPercent;
	}
}
