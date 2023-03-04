package ru.kslacker.banks.bankaccounts.accounttypes;

import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.SuspiciousLimitingAccountTypeBase;
import ru.kslacker.banks.models.MoneyAmount;
import java.math.BigDecimal;
import java.time.Period;

public class DebitAccountTypeImpl extends SuspiciousLimitingAccountTypeBase implements
	DebitAccountType {

	private BigDecimal interestPercent;
	private Period interestCalculationPeriod;

	/**
	 * Constructor of basic implementation of debit account type
	 *
	 * @param interestPercent                   annual interest on balance percent
	 * @param interestCalculationPeriod         period of interest calculation
	 * @param suspiciousAccountsOperationsLimit limit on operations with suspicious accounts
	 */
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
