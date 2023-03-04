package ru.kslacker.banks.accounttypemanager.api;

import ru.kslacker.banks.bankaccounts.accounttypes.api.AccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.CreditAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DebitAccountType;
import ru.kslacker.banks.bankaccounts.accounttypes.api.DepositAccountType;
import ru.kslacker.banks.models.InterestOnBalancePolicy;
import ru.kslacker.banks.models.MoneyAmount;

import java.math.BigDecimal;
import java.time.Period;
import java.util.UUID;

public abstract class AccountTypeManagerWrapper implements AccountTypeManager {

	private final AccountTypeManager wrapped;

	/**
	 * AccountTypeManagerWrapper constructor
	 *
	 * @param wrapped wrapped type manager
	 */
	public AccountTypeManagerWrapper(AccountTypeManager wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public AccountType getAccountType(UUID id) {
		return wrapped.getAccountType(id);
	}

	@Override
	public CreditAccountType createCreditAccountType(MoneyAmount debtLimit, MoneyAmount charge) {
		return wrapped.createCreditAccountType(debtLimit, charge);
	}

	@Override
	public void setDebtLimit(UUID creditTypeId, MoneyAmount debtLimit) {
		wrapped.setDebtLimit(creditTypeId, debtLimit);
	}

	@Override
	public void setCharge(UUID creditTypeId, MoneyAmount charge) {
		wrapped.setCharge(creditTypeId, charge);
	}

	@Override
	public DebitAccountType createDebitAccountType(
		BigDecimal interestOnBalance,
		Period interestCalculationPeriod) {

		return wrapped.createDebitAccountType(interestOnBalance, interestCalculationPeriod);
	}

	@Override
	public void setInterestOnBalance(UUID debitTypeId, BigDecimal interestOnBalance) {
		wrapped.setInterestOnBalance(debitTypeId, interestOnBalance);
	}

	@Override
	public DepositAccountType createDepositAccountType(
		Period depositTerm,
		InterestOnBalancePolicy interestOnBalancePolicy,
		Period interestCalculationPeriod) {

		return wrapped.createDepositAccountType(
			depositTerm,
			interestOnBalancePolicy,
			interestCalculationPeriod);
	}

	@Override
	public void setInterestOnBalancePolicy(UUID depositTypeId, InterestOnBalancePolicy policy) {
		wrapped.setInterestOnBalancePolicy(depositTypeId, policy);
	}

	@Override
	public void setDepositTerm(UUID depositTypeId, Period depositTerm) {
		wrapped.setDepositTerm(depositTypeId, depositTerm);
	}

	@Override
	public void setInterestCalculationPeriod(UUID typeId, Period interestCalculationPeriod) {
		wrapped.setInterestCalculationPeriod(typeId, interestCalculationPeriod);
	}

	@Override
	public MoneyAmount getSuspiciousAccountsOperationsLimit() {
		return wrapped.getSuspiciousAccountsOperationsLimit();
	}

	@Override
	public void setSuspiciousOperationsLimit(MoneyAmount limit) {
		wrapped.setSuspiciousOperationsLimit(limit);
	}

}
