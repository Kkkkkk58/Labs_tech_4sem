package ru.kslacker.banks.bankaccounts.accounttypes;

import java.math.BigDecimal;
import java.time.Period;

import lombok.Getter;
import lombok.Setter;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.SuspiciousLimitingAccountTypeBase;
import ru.kslacker.banks.models.InterestOnBalanceLayer;
import ru.kslacker.banks.models.InterestOnBalancePolicy;
import ru.kslacker.banks.models.MoneyAmount;

@Getter
@Setter
public class DepositAccountTypeImpl extends SuspiciousLimitingAccountTypeBase implements DepositAccountType {

	private InterestOnBalancePolicy interestOnBalancePolicy;
	private Period depositTerm;
	private Period interestCalculationPeriod;

	public DepositAccountTypeImpl(
		InterestOnBalancePolicy interestOnBalancePolicy,
		Period depositTerm,
		Period interestCalculationPeriod,
		MoneyAmount suspiciousAccountsOperationsLimit) {

		super(suspiciousAccountsOperationsLimit);
		this.interestOnBalancePolicy = interestOnBalancePolicy;
		this.depositTerm = depositTerm;
		this.interestCalculationPeriod = interestCalculationPeriod;
	}

	@Override
	public BigDecimal getInterestPercent(MoneyAmount initialBalance) {
		return interestOnBalancePolicy
			.getLayers()
			.stream()
			.filter(layer -> layer.requiredInitialBalance().compareTo(initialBalance) <= 0)
			.reduce((a, b) -> b)
			.map(InterestOnBalanceLayer::interestOnBalance)
			.orElse(BigDecimal.ZERO);
	}
}
