package ru.kslacker.banks.models;

import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.Setter;
import ru.kslacker.banks.bankaccounts.CommandExecutingBankAccount;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OperationInformation implements ReadOnlyOperationInformation {

	@Include
	private final UUID id;
	private final CommandExecutingBankAccount account;
	private MoneyAmount operatedAmount;
	private final LocalDateTime initTime;
	private LocalDateTime completionTime;

	public OperationInformation(CommandExecutingBankAccount account, MoneyAmount operatedAmount, LocalDateTime initTime) {
		this.id = UUID.randomUUID();
		this.account = account;
		this.operatedAmount = operatedAmount;
		this.initTime = initTime;
	}

	@Override
	public UUID getAccountId() {
		return account.getId();
	}
}
