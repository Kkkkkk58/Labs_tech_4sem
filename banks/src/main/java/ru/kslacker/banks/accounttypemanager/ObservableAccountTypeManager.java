package ru.kslacker.banks.accounttypemanager;

import ru.kslacker.banks.accounttypemanager.api.AccountTypeManager;
import ru.kslacker.banks.accounttypemanager.api.AccountTypeManagerWrapper;
import ru.kslacker.banks.eventargs.BankAccountTypeChangesEventArgs;
import ru.kslacker.banks.models.InterestOnBalancePolicy;
import ru.kslacker.banks.models.MoneyAmount;
import ru.kslacker.banks.tools.eventhandling.EventHandler;

import java.math.BigDecimal;
import java.time.Period;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ObservableAccountTypeManager extends AccountTypeManagerWrapper {

	private final EventHandler<BankAccountTypeChangesEventArgs> updateHandler;

	/**
	 * Constructor of AccountTypeManager that sends notifications to customers about changes in
	 * account types
	 *
	 * @param wrapped wrapped AccountTypeManager
	 * @param updater action to perform when changes happen
	 */
	public ObservableAccountTypeManager(
		AccountTypeManager wrapped,
		BiConsumer<Object, BankAccountTypeChangesEventArgs> updater) {
		super(wrapped);

		this.updateHandler = new EventHandler<>();
		updateHandler.addListener(updater);
	}

	@Override
	public void setInterestOnBalance(UUID debitTypeId, BigDecimal interestOnBalance) {
		super.setInterestOnBalance(debitTypeId, interestOnBalance);

		updateHandler.invoke(
			this,
			new BankAccountTypeChangesEventArgs(getAccountType(debitTypeId),
				"New interest on balance is " + interestOnBalance));
	}

	@Override
	public void setDebtLimit(UUID creditTypeId, MoneyAmount debtLimit) {
		super.setDebtLimit(creditTypeId, debtLimit);

		updateHandler.invoke(
			this,
			new BankAccountTypeChangesEventArgs(getAccountType(creditTypeId),
				"New debt limit is " + debtLimit));
	}

	@Override
	public void setCharge(UUID creditTypeId, MoneyAmount charge) {
		super.setCharge(creditTypeId, charge);

		updateHandler.invoke(
			this,
			new BankAccountTypeChangesEventArgs(getAccountType(creditTypeId),
				"New credit charge is " + charge));
	}

	@Override
	public void setInterestOnBalancePolicy(UUID depositTypeId, InterestOnBalancePolicy policy) {
		super.setInterestOnBalancePolicy(depositTypeId, policy);

		updateHandler.invoke(this,
			new BankAccountTypeChangesEventArgs(getAccountType(depositTypeId),
				"New interest on balance policy is here!"));
	}

	@Override
	public void setDepositTerm(UUID depositTypeId, Period depositTerm) {
		super.setDepositTerm(depositTypeId, depositTerm);

		updateHandler.invoke(this,
			new BankAccountTypeChangesEventArgs(getAccountType(depositTypeId),
				"New deposit term is " + depositTerm));
	}

	@Override
	public void setInterestCalculationPeriod(UUID typeId, Period interestCalculationPeriod) {
		super.setInterestCalculationPeriod(typeId, interestCalculationPeriod);

		updateHandler.invoke(
			this,
			new BankAccountTypeChangesEventArgs(getAccountType(typeId),
				"New interest calculation period is " + interestCalculationPeriod));
	}
}
