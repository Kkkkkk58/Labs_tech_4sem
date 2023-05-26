package ru.kslacker.cats.microservices.common.amqp.api;

public interface AmqpRelyingService {

	<T, R> R handleRequest(String routingKey, T message, Class<R> returnType);
}
