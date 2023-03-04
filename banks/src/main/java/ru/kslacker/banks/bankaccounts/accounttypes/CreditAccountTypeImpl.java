package ru.kslacker.banks.bankaccounts.accounttypes;

import lombok.Getter;
import lombok.Setter;
import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.SuspiciousLimitingAccountTypeBase;
import ru.kslacker.banks.models.MoneyAmount;

@Getter
@Setter
public class CreditAccountTypeImpl extends SuspiciousLimitingAccountTypeBase implements
	CreditAccountType {

	private MoneyAmount debtLimit;
	private MoneyAmount charge;

	/**
	 * Constructor of basic implementation of credit account type
	 *
	 * @param debtLimit                         limit on debt, after reaching the limit charge is
	 *                                          taken
	 * @param charge                            value of charge to take after reaching the debt
	 *                                          limit
	 * @param suspiciousAccountsOperationsLimit limit on operations with suspicious account
	 */
	public CreditAccountTypeImpl(
		MoneyAmount debtLimit,
		MoneyAmount charge,
		MoneyAmount suspiciousAccountsOperationsLimit) {
		super(suspiciousAccountsOperationsLimit);
		this.debtLimit = debtLimit;
		this.charge = charge;
	}
}
