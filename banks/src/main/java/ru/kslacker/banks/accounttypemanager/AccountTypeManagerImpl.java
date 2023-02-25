package ru.kslacker.banks.accounttypemanager;

import lombok.experimental.ExtensionMethod;
import ru.kslacker.banks.accounttypemanager.api.AccountTypeManager;
import ru.kslacker.banks.bankaccounts.accounttypes.CreditAccountTypeImpl;
import ru.kslacker.banks.bankaccounts.accounttypes.DebitAccountTypeImpl;
import ru.kslacker.banks.bankaccounts.accounttypes.DepositAccountTypeImpl;
import ru.kslacker.banks.bankaccounts.accounttypes.api.*;
import ru.kslacker.banks.exceptions.AccountTypeManagerException;
import ru.kslacker.banks.models.InterestOnBalancePolicy;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.tools.extensions.StreamExtensions;
import java.math.BigDecimal;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtensionMethod(StreamExtensions.class)
public class AccountTypeManagerImpl implements AccountTypeManager {

	private final List<AccountType> types;
	private MoneyAmount suspiciousAccountsOperationsLimit;

	/**
	 * Constructor of basic AccountTypeManager implementation
	 *
	 * @param suspiciousOperationsLimit limit on transactions of suspicious accounts
	 */
	public AccountTypeManagerImpl(MoneyAmount suspiciousOperationsLimit) {
		this.types = new ArrayList<>();
		this.suspiciousAccountsOperationsLimit = suspiciousOperationsLimit;
	}

	@Override
	public MoneyAmount getSuspiciousAccountsOperationsLimit() {
		return suspiciousAccountsOperationsLimit;
	}

	@Override
	public void setSuspiciousOperationsLimit(MoneyAmount limit) {
		this.suspiciousAccountsOperationsLimit = limit;
		for (AccountType accountType : types) {
			if (accountType instanceof SuspiciousLimitingAccountType type) {
				type.setSuspiciousAccountsOperationsLimit(limit);
			}
		}
	}

	@Override
	public DebitAccountType createDebitAccountType(
		BigDecimal interestOnBalance,
		Period interestCalculationPeriod) {

		DebitAccountType type = new DebitAccountTypeImpl(
			interestOnBalance,
			interestCalculationPeriod,
			suspiciousAccountsOperationsLimit);
		types.add(type);

		return type;
	}

	@Override
	public void setInterestOnBalance(UUID debitTypeId, BigDecimal interestOnBalance) {
		if (interestOnBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}
		if (!(getAccountType(debitTypeId) instanceof DebitAccountType debitType)) {
			throw AccountTypeManagerException.invalidAccountType();
		}

		debitType.setInterestPercent(interestOnBalance);
	}

	@Override
	public AccountType getAccountType(UUID id) {
		return types.stream()
			.single(type -> type.getId().equals(id));
	}

	@Override
	public CreditAccountType createCreditAccountType(MoneyAmount debtLimit, MoneyAmount charge) {
		CreditAccountType type = new CreditAccountTypeImpl(
			debtLimit,
			charge,
			suspiciousAccountsOperationsLimit);
		types.add(type);

		return type;
	}

	@Override
	public void setDebtLimit(UUID creditTypeId, MoneyAmount debtLimit) {
		if (!(getAccountType(creditTypeId) instanceof CreditAccountType creditType)) {
			throw AccountTypeManagerException.invalidAccountType();
		}

		creditType.setDebtLimit(debtLimit);
	}

	@Override
	public void setCharge(UUID creditTypeId, MoneyAmount charge) {
		if (!(getAccountType(creditTypeId) instanceof CreditAccountType creditType)) {
			throw AccountTypeManagerException.invalidAccountType();
		}

		creditType.setCharge(charge);
	}

	@Override
	public DepositAccountType createDepositAccountType(
		Period depositTerm,
		InterestOnBalancePolicy interestOnBalancePolicy,
		Period interestCalculationPeriod) {

		DepositAccountType type = new DepositAccountTypeImpl(
			interestOnBalancePolicy,
			depositTerm,
			interestCalculationPeriod,
			suspiciousAccountsOperationsLimit);
		types.add(type);

		return type;
	}

	@Override
	public void setInterestOnBalancePolicy(UUID depositTypeId, InterestOnBalancePolicy policy) {
		if (!(getAccountType(depositTypeId) instanceof DepositAccountType type)) {
			throw AccountTypeManagerException.invalidAccountType();
		}

		type.setInterestOnBalancePolicy(policy);
	}

	@Override
	public void setDepositTerm(UUID depositTypeId, Period depositTerm) {
		if (!(getAccountType(depositTypeId) instanceof DepositAccountType type)) {
			throw AccountTypeManagerException.invalidAccountType();
		}

		type.setDepositTerm(depositTerm);
	}

	@Override
	public void setInterestCalculationPeriod(UUID typeId, Period interestCalculationPeriod) {
		if (!(getAccountType(typeId) instanceof InterestCalculationAccountType type)) {
			throw AccountTypeManagerException.invalidAccountType();
		}

		type.setInterestCalculationPeriod(interestCalculationPeriod);
	}
}
