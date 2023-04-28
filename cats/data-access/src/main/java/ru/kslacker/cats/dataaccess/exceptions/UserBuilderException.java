package ru.kslacker.cats.dataaccess.exceptions;

import ru.kslacker.cats.common.exceptions.CatsException;

public class UserBuilderException extends CatsException {

	private UserBuilderException(String message) {
		super(message);
	}

	public static UserBuilderException missingCredentials() {
		return new UserBuilderException("Credentials for new user were not provided");
	}
}
