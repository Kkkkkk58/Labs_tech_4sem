package ru.kslacker.cats.services.exceptions;

import ru.kslacker.cats.common.exceptions.CatsException;

public class UserException extends CatsException {

	private UserException(String message) {
		super(message);
	}

	public static UserException userAlreadyDisabled(Long id) {
		return new UserException("User with id " + id + " is already disabled");
	}

	public static UserException userAlreadyEnabled(Long id) {
		return new UserException("User with id " + id + " is already enabled");
	}

	public static UserException userAlreadyLocked(Long id) {
		return new UserException("User with id " + id + " is already locked");
	}

	public static UserException userAlreadyUnlocked(Long id) {
		return new UserException("User with id " + id + " is already unlocked");
	}

	public static UserException userAlreadyAdmin(Long id) {
		return new UserException("User with id " + id + " is already admin");
	}
}
