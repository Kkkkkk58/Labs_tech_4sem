package ru.kslacker.banks.bankaccounts.accounttypes;

import lombok.Getter;
import lombok.Setter;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.SuspiciousLimitingAccountTypeBase;
import ru.kslacker.banks.models.InterestOnBalanceLayer;
import ru.kslacker.banks.models.InterestOnBalancePolicy;
import ru.kslacker.banks.models.MoneyAmount;
import java.math.BigDecimal;
import java.time.Period;

@Getter
@Setter
public class DepositAccountTypeImpl
	extends SuspiciousLimitingAccountTypeBase
	implements DepositAccountType {

	private InterestOnBalancePolicy interestOnBalancePolicy;
	private Period depositTerm;
	private Period interestCalculationPeriod;

	/**
	 * Constructor of basic implementation of deposit account type
	 *
	 * @param interestOnBalancePolicy           set of rules to determine interest on balance
	 *                                          percent based on initial balance of account
	 * @param depositTerm                       term of deposit, time limit of withdrawal
	 * @param interestCalculationPeriod         period of interest calculation
	 * @param suspiciousAccountsOperationsLimit limit on operations with suspicious accounts
	 */
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
