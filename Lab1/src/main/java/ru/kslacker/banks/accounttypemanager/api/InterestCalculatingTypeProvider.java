package ru.kslacker.banks.accounttypemanager.api;


import java.time.Period;
import java.util.UUID;

public interface InterestCalculatingTypeProvider {

	void setInterestCalculationPeriod(UUID typeId, Period interestCalculationPeriod);
}
