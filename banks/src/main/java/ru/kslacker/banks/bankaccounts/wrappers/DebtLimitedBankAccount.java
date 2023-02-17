package ru.kslacker.banks.bankaccounts.wrappers;

import ru.kslacker.banks.bankaccounts.BankAccount;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebtLimitedAccountType;
import ru.kslacker.banks.exceptions.AccountWrapperException;
import ru.kslacker.banks.exceptions.TransactionException;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.transactions.Transaction;
import java.math.BigDecimal;
import java.util.Objects;

public class DebtLimitedBankAccount extends BankAccountWrapper {

	private final DebtLimitedAccountType type;

	public DebtLimitedBankAccount(BankAccount wrapped) {
		super(wrapped);

		if (!(wrapped.getType() instanceof DebtLimitedAccountType accountType)) {
			throw AccountWrapperException.invalidWrappedType();
		}

		this.type = accountType;
	}

	@Override
	public MoneyAmount withdraw(Transaction transaction)
	{
		MoneyAmount moneyAmount = transaction.getInformation().getOperatedAmount();
		MoneyAmount debt = estimateNewDebtValue(moneyAmount);

		if (debt.compareTo(type.getDebtLimit()) >= 0) {
			throw TransactionException.debtIsOverTheLimit(debt, type.getDebtLimit());
		}

		return super.withdraw(transaction);
	}

	private MoneyAmount estimateNewDebtValue(MoneyAmount moneyAmount)
	{
		var debt = new MoneyAmount(BigDecimal.ZERO, type.getDebtLimit().currency());
		if (!Objects.equals(getDebt().value(), BigDecimal.ZERO))
		{
			debt = getDebt().add(moneyAmount);
		}
		else if (getBalance().compareTo(moneyAmount) < 0)
		{
			debt = moneyAmount.subtract(getBalance());
		}

		return debt;
	}
}
