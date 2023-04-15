package ru.kslacker.cats.services.exceptions;

import ru.kslacker.cats.common.exceptions.CatsException;

public class EntityException extends CatsException {

	private EntityException(String message) {
		super(message);
	}

	public static <T, ID> EntityException entityNotFound(Class<T> clazz, ID id) {
		return new EntityException(
			"Entity of type " + clazz.getName() + " with id " + id + " not found");
	}
}
