package ru.kslacker.cats.microservices.catowners.validation.service.api;

public interface ValidationService {

	<T> void validate(T t);
}

