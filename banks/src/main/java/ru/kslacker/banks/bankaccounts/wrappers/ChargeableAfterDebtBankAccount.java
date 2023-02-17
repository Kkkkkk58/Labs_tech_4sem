package ru.kslacker.banks.bankaccounts.wrappers;

import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.ChargeableAccountType;
import ru.kslacker.banks.exceptions.AccountWrapperException;
import ru.kslacker.banks.exceptions.TransactionException;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.transactions.Transaction;
import java.math.BigDecimal;
import java.util.Objects;

public class ChargeableAfterDebtBankAccount extends BankAccountWrapper {

	private final ChargeableAccountType type;

	public ChargeableAfterDebtBankAccount(BankAccount wrapped) {
		super(wrapped);

		if (!(wrapped.getType() instanceof ChargeableAccountType accountType)) {
			throw AccountWrapperException.invalidWrappedType();
		}

		this.type = accountType;
	}

	@Override
	public MoneyAmount withdraw(Transaction transaction) {
		applyWithdrawalCharge(transaction);
		return super.withdraw(transaction);
	}

	@Override
	public void replenish(Transaction transaction) {
		applyReplenishCharge(transaction);
		super.replenish(transaction);
	}

	private void applyWithdrawalCharge(Transaction transaction)
	{
		if (Objects.equals(getDebt().value(), BigDecimal.ZERO))
			return;

		MoneyAmount moneyAmount = transaction.getInformation().getOperatedAmount();
		transaction.getInformation().setOperatedAmount(moneyAmount.add(type.getCharge()));
	}

	private void applyReplenishCharge(Transaction transaction)
	{
		if (Objects.equals(getDebt().value(), BigDecimal.ZERO)) {
			return;
		}

		MoneyAmount moneyAmount = transaction.getInformation().getOperatedAmount();
		if (moneyAmount.compareTo(type.getCharge()) < 0) {
			throw TransactionException.chargeRateExceedsMoneyAmount(moneyAmount, type.getCharge());
		}

		transaction.getInformation().setOperatedAmount(moneyAmount.subtract(type.getCharge()));
	}
}
