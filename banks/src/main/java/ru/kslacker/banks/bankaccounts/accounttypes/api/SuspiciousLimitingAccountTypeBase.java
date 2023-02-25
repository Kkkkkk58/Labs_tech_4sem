package ru.kslacker.banks.bankaccounts.accounttypes.api;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import ru.kslacker.banks.models.MoneyAmount;

@Getter
@Setter
public abstract class SuspiciousLimitingAccountTypeBase implements SuspiciousLimitingAccountType {

	private final UUID id;
	private MoneyAmount suspiciousAccountsOperationsLimit;

	/**
	 * Constructor of abstract base class with limits on suspicious operations
	 *
	 * @param suspiciousAccountsOperationsLimit the limit
	 */
	protected SuspiciousLimitingAccountTypeBase(MoneyAmount suspiciousAccountsOperationsLimit) {
		this.id = UUID.randomUUID();
		this.suspiciousAccountsOperationsLimit = suspiciousAccountsOperationsLimit;
	}
}
