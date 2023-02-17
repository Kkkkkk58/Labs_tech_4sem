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

	protected SuspiciousLimitingAccountTypeBase(MoneyAmount suspiciousAccountsOperationsLimit) {
		this.id = UUID.randomUUID();
		this.suspiciousAccountsOperationsLimit = suspiciousAccountsOperationsLimit;
	}
}
