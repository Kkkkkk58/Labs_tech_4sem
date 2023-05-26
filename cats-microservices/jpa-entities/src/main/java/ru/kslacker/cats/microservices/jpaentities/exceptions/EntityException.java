package ru.kslacker.cats.microservices.jpaentities.exceptions;

public class EntityException extends RuntimeException {

	public EntityException(String message) {
		super(message);
	}

	public static <T, ID> EntityException entityNotFound(Class<T> clazz, ID id) {
		return new EntityException(
			"Entity of type " + clazz.getName() + " with getId " + id + " not found");
	}
}
