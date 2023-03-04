package ru.kslacker.banks.exceptions;

import ru.kslacker.banks.models.MoneyAmount;

public class TransactionException extends BanksDomainException {

	private TransactionException(String message) {
		super(message);
	}

	/**
	 * Exception thrown when charge exceeds transaction amount
	 *
	 * @param moneyAmount transaction amount
	 * @param charge      charge value
	 * @return exception with corresponding message
	 */
	public static TransactionException chargeRateExceedsMoneyAmount(
		MoneyAmount moneyAmount,
		MoneyAmount charge) {

		return new TransactionException(
			"Charge " + charge + " exceeds transaction money amount " + moneyAmount);
	}

	/**
	 * Exception thrown when debt is over the debt limit
	 *
	 * @param debt      debt value
	 * @param debtLimit debt limit
	 * @return exception with corresponding message
	 */
	public static TransactionException debtIsOverTheLimit(MoneyAmount debt, MoneyAmount debtLimit) {
		return new TransactionException(
			"Debt " + debt + " value is over the limited debt " + debtLimit
				+ " value after the transaction");
	}

	/**
	 * Exception thrown when balance would become negative after transaction
	 *
	 * @return exception with corresponding message
	 */
	public static TransactionException negativeBalance() {
		return new TransactionException(
			"Can't perform this transaction because the balance would be negative");
	}

	/**
	 * Exception thrown when suspicious operations limit is exceeded
	 *
	 * @param moneyAmount                       transaction amount
	 * @param suspiciousAccountsOperationsLimit suspicious operations limit
	 * @return exception with corresponding message
	 */
	public static TransactionException suspiciousOperationsLimitExceeded(
		MoneyAmount moneyAmount,
		MoneyAmount suspiciousAccountsOperationsLimit) {

		return new TransactionException(
			"Suspicious operation limit is exceeded: was " + moneyAmount + ", limit is "
				+ suspiciousAccountsOperationsLimit);
	}

	/**
	 * Exception thrown when withdrawal happens before the end of deposit term
	 *
	 * @return exception with corresponding message
	 */
	public static TransactionException withdrawnBeforeLimit() {
		return new TransactionException("Can't withdraw money until the time limit is reached");
	}
}
