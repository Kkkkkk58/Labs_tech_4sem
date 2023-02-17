package ru.kslacker.banks.models;

import lombok.Data;
import java.time.LocalDate;

@Data
public abstract class DocumentData {

	private final LocalDate dateOfIssue;
	private final String number;
}
