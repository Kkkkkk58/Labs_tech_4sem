package ru.kslacker.cats.microservices.jpaentities.exceptions;

public class UserBuilderException extends RuntimeException {

	public UserBuilderException(String message) {
		super(message);
	}

	public static UserBuilderException missingCredentials() {
		return new UserBuilderException("Credentials for new user were not provided");
	}
}
