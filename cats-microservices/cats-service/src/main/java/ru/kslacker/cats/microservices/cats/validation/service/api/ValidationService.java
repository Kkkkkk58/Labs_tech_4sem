package ru.kslacker.cats.microservices.cats.validation.service.api;

public interface ValidationService {

	<T> void validate(T t);
}

