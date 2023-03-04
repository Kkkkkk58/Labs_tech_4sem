package ru.kslacker.banks.console.representation;

import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.models.ReadOnlyOperationInformation;
import java.time.LocalDateTime;
import java.util.UUID;

public class OperationInformationRepresentation {

	private final UUID operationId;
	private final UUID accountId;
	private final MoneyAmount operatedAmount;
	private final boolean isCompleted;
	private final LocalDateTime initTime;
	private final LocalDateTime completionTime;

	public OperationInformationRepresentation(ReadOnlyOperationInformation info) {
		this.operationId = info.getId();
		this.accountId = info.getAccountId();
		this.operatedAmount = info.getOperatedAmount();
		this.isCompleted = info.isCompleted();
		this.initTime = info.getInitTime();
		this.completionTime = info.getCompletionTime();
	}

	@Override
	public String toString() {
		return
			"Operation " + operationId + " with account " + accountId + " and money amount " + operatedAmount + "\n" +
			"Is completed: " + isCompleted + "\n" +
			"Performing time: " + initTime + " - " + completionTime;
	}
}
