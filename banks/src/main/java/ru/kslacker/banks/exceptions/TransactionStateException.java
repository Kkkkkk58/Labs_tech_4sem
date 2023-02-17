package ru.kslacker.banks.exceptions;

import java.util.UUID;

public class TransactionStateException extends BanksDomainException {

	private TransactionStateException(String message) {
		super(message);
	}

	public static TransactionStateException multipleCancelling(UUID commandId)
	{
		return new TransactionStateException("Cannot cancel operation " + commandId + " twice");
	}

	public static TransactionStateException performingAfterCompletion(UUID commandId)
	{
		return new TransactionStateException("Cannot perform completed operation " + commandId);
	}

	public static TransactionStateException cancellingFailedOperation(UUID commandId)
	{
		return new TransactionStateException("Cannot cancel failed operation " + commandId);
	}

	public static TransactionStateException cancellingNewOperation(UUID commandId)
	{
		return new TransactionStateException("Cannot cancel an operation " + commandId + " that wasn't performed");
	}

	public static TransactionStateException alreadyRunningOperation(UUID commandId)
	{
		return new TransactionStateException("Operation " + commandId + " is already running");
	}
}
