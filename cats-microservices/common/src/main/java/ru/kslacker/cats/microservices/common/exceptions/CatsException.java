package ru.kslacker.cats.microservices.common.exceptions;

public class CatsException extends RuntimeException {

	public CatsException(String message) {
		super(message);
	}
}
