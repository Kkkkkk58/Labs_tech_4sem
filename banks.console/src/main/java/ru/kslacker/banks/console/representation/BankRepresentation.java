package ru.kslacker.banks.console.representation;

import ru.kslacker.banks.entities.api.NoTransactionalBank;
import java.util.UUID;

public class BankRepresentation {

	private final String name;
	private final UUID id;

	public BankRepresentation(NoTransactionalBank bank) {
		this.name = bank.getName();
		this.id = bank.getId();
	}

	@Override
	public String toString() {
		return "Bank " + name + " [" + id + "]";
	}
}
