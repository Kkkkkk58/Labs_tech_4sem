package ru.kslacker.banks.exceptions;

import ru.kslacker.banks.models.MoneyAmount;

public class TransactionException extends BanksDomainException {

	private TransactionException(String message) {
		super(message);
	}

	public static TransactionException chargeRateExceedsMoneyAmount(MoneyAmount moneyAmount, MoneyAmount charge)
	{
		return new TransactionException("Charge " + charge + " exceeds transaction money amount " + moneyAmount);
	}

	public static TransactionException debtIsOverTheLimit(MoneyAmount debt, MoneyAmount debtLimit)
	{
		return new TransactionException(
			"Debt " + debt + " value is over the limited debt " + debtLimit + " value after the transaction");
	}

	public static TransactionException negativeBalance()
	{
		return new TransactionException("Can't perform this transaction because the balance would be negative");
	}

	public static TransactionException suspiciousOperationsLimitExceeded(
		MoneyAmount moneyAmount,
		MoneyAmount suspiciousAccountsOperationsLimit)
	{
		return new TransactionException(
			"Suspicious operation limit is exceeded: was " + moneyAmount + ", limit is " + suspiciousAccountsOperationsLimit);
	}

	public static TransactionException withdrawnBeforeLimit()
	{
		return new TransactionException("Can't withdraw money until the time limit is reached");
	}
}
