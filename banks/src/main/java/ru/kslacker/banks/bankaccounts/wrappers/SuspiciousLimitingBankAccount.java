package ru.kslacker.banks.bankaccounts.wrappers;

import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.SuspiciousLimitingAccountType;
import ru.kslacker.banks.exceptions.AccountWrapperException;
import ru.kslacker.banks.exceptions.TransactionException;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.transactions.Transaction;

public class SuspiciousLimitingBankAccount extends BankAccountWrapper {

	private final BankAccount wrapped;
	private final SuspiciousLimitingAccountType type;

	/**
	 * Bank account that has limits on operations with suspicious accounts
	 *
	 * @param wrapped wrapped bank account
	 */
	public SuspiciousLimitingBankAccount(BankAccount wrapped) {
		super(wrapped);

		if (!(wrapped.getType() instanceof SuspiciousLimitingAccountType accountType)) {
			throw AccountWrapperException.invalidWrappedType();
		}

		this.type = accountType;
		this.wrapped = wrapped;
	}

	@Override
	public MoneyAmount withdraw(Transaction transaction) {

		validateTransaction(transaction);
		return super.withdraw(transaction);
	}

	@Override
	public void replenish(Transaction transaction) {

		validateTransaction(transaction);
		super.replenish(transaction);
	}

	private void validateTransaction(Transaction transaction) {
		if (isSuspiciousAccountLimitExceeded(transaction.getInformation().getOperatedAmount())) {
			throw TransactionException.suspiciousOperationsLimitExceeded(
				transaction.getInformation().getOperatedAmount(),
				type.getSuspiciousAccountsOperationsLimit());
		}
	}

	private boolean isSuspiciousAccountLimitExceeded(MoneyAmount moneyAmount) {
		return wrapped.isSuspicious()
			&& moneyAmount.compareTo(type.getSuspiciousAccountsOperationsLimit()) > 0;
	}
}
