package ru.kslacker.cats.microservices.restapi.services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.springframework.amqp.AmqpTimeoutException;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.core.ParameterizedTypeReference;
import ru.kslacker.cats.microservices.common.exceptions.TypeAwareExceptionWrapper;

public abstract class AmqpRelyingService {

	private final AsyncAmqpTemplate asyncAmqpTemplate;
	private final Exchange exchange;
	private final ObjectMapper objectMapper;

	protected AmqpRelyingService(
		AsyncAmqpTemplate asyncAmqpTemplate,
		Exchange exchange,
		ObjectMapper objectMapper) {

		this.asyncAmqpTemplate = asyncAmqpTemplate;
		this.exchange = exchange;
		this.objectMapper = objectMapper;
	}

	@SneakyThrows
	protected <T, R> R handleRequest(String routingKey, T message, Class<R> clazz) {

		CompletableFuture<R> future = asyncAmqpTemplate.convertSendAndReceiveAsType(
			exchange.getName(),
			routingKey,
			message,
			new ParameterizedTypeReference<>() {
			}
		);

		try {
			assert future != null;
			Object value = future.get();
			try {
				return objectMapper.convertValue(value, clazz);
			} catch (IllegalArgumentException ignored) {
				TypeAwareExceptionWrapper<?> exception = objectMapper.convertValue(value, TypeAwareExceptionWrapper.class);
				throw exception.getClazz().getDeclaredConstructor(String.class).newInstance(exception.getException().getMessage());
			}
		} catch (InterruptedException e) {
			throw new AmqpTimeoutException("Timeout reached");
		}
	}

}
