package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class TransactionStateException extends BanksDomainException {

	private TransactionStateException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when trying to cancel an operation that was already cancelled
	 *
	 * @param commandId id of executed command
	 * @return exception with corresponding message
	 */
	public static TransactionStateException multipleCancelling(UUID commandId) {
		return new TransactionStateException("Cannot cancel operation " + commandId + " twice");
	}

	/**
	 * Exception thrown when trying to perform an operation that was already completed
	 *
	 * @param commandId id of executed command
	 * @return exception with corresponding message
	 */
	public static TransactionStateException performingAfterCompletion(UUID commandId) {
		return new TransactionStateException("Cannot perform completed operation " + commandId);
	}

	/**
	 * Exception thrown when trying to cancel failed operation
	 *
	 * @param commandId id of executed command
	 * @return exception with corresponding message
	 */
	public static TransactionStateException cancellingFailedOperation(UUID commandId) {
		return new TransactionStateException("Cannot cancel failed operation " + commandId);
	}

	/**
	 * Exception thrown when trying to cancel operation that wasn't performed
	 *
	 * @param commandId id of executed command
	 * @return exception with corresponding message
	 */
	public static TransactionStateException cancellingNewOperation(UUID commandId) {
		return new TransactionStateException(
			"Cannot cancel an operation " + commandId + " that wasn't performed");
	}

	/**
	 * Exception thrown when trying to run transaction that is already running
	 *
	 * @param commandId id of executed command
	 * @return exception with corresponding message
	 */
	public static TransactionStateException alreadyRunningOperation(UUID commandId) {
		return new TransactionStateException("Operation " + commandId + " is already running");
	}
}
