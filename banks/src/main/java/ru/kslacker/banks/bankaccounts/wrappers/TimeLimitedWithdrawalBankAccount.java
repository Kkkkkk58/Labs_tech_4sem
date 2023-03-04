package ru.kslacker.banks.bankaccounts.wrappers;

import java.time.Clock;
import java.time.LocalDateTime;
import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.TimeLimitedWithdrawalAccountType;
import ru.kslacker.banks.exceptions.AccountWrapperException;
import ru.kslacker.banks.exceptions.TransactionException;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.transactions.Transaction;

public class TimeLimitedWithdrawalBankAccount extends BankAccountWrapper {

	private final TimeLimitedWithdrawalAccountType type;
	private final Clock clock;

	/**
	 * Bank account that has time limits on withdrawal
	 *
	 * @param wrapped wrapped bank account
	 * @param clock   clock instance to track time
	 */
	public TimeLimitedWithdrawalBankAccount(BankAccount wrapped, Clock clock) {

		super(wrapped);

		if (!(wrapped.getType() instanceof TimeLimitedWithdrawalAccountType accountType)) {
			throw AccountWrapperException.invalidWrappedType();
		}

		this.type = accountType;
		this.clock = clock;
	}

	@Override
	public MoneyAmount withdraw(Transaction transaction) {

		if (isWithdrawnBeforeLimit()) {
			throw TransactionException.withdrawnBeforeLimit();
		}
		return super.withdraw(transaction);
	}

	private boolean isWithdrawnBeforeLimit() {
		return getCreationDate().plus(type.getDepositTerm()).isBefore(LocalDateTime.now(clock));
	}
}
