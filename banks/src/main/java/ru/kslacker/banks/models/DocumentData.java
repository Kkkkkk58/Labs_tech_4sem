package ru.kslacker.banks.models;

import java.time.LocalDate;
import lombok.Data;

@Data
public abstract class DocumentData {

	private final LocalDate dateOfIssue;
	private final String number;
}
