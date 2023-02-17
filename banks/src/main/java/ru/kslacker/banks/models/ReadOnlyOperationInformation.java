package ru.kslacker.banks.models;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ReadOnlyOperationInformation {
	UUID getId();
	UUID getAccountId();
	MoneyAmount getOperatedAmount();
	LocalDateTime getInitTime();
	LocalDateTime getCompletionTime();
	default boolean isCompleted() {
		return getCompletionTime() != null;
	}

}
