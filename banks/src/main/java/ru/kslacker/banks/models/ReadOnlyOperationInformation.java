package ru.kslacker.banks.models;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ReadOnlyOperationInformation {

	/**
	 * Method to get operation id
	 *
	 * @return operation id
	 */
	UUID getId();

	/**
	 * Method to get account id
	 *
	 * @return account id
	 */
	UUID getAccountId();

	/**
	 * Method to get money amount
	 *
	 * @return money amount
	 */
	MoneyAmount getOperatedAmount();

	/**
	 * Method to get the time when the operation was launched
	 *
	 * @return operation initialization time
	 */
	LocalDateTime getInitTime();

	/**
	 * Method to get the time when the operation was completed
	 *
	 * @return operation completion time
	 */
	LocalDateTime getCompletionTime();

	/**
	 * Method to check whether the operation is completed
	 *
	 * @return true if the operation is completed
	 */
	default boolean isCompleted() {
		return getCompletionTime() != null;
	}

}
