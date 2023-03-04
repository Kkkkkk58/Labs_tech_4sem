package ru.kslacker.banks.models;

import java.time.LocalDate;

public class PassportData extends DocumentData {

	public PassportData(LocalDate dateOfIssue, String number) {
		super(dateOfIssue, number);
	}
}
