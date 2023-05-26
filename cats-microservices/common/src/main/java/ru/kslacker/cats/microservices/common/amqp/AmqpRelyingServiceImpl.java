package ru.kslacker.cats.microservices.common.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.AmqpTimeoutException;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.core.ParameterizedTypeReference;
import ru.kslacker.cats.microservices.common.amqp.api.AmqpRelyingService;
import java.util.concurrent.CompletableFuture;

public class AmqpRelyingServiceImpl implements AmqpRelyingService {

	private final AsyncAmqpTemplate asyncAmqpTemplate;
	private final Exchange exchange;
	private final ObjectMapper objectMapper;

	public AmqpRelyingServiceImpl(
		AsyncAmqpTemplate asyncAmqpTemplate,
		Exchange exchange,
		ObjectMapper objectMapper) {

		this.asyncAmqpTemplate = asyncAmqpTemplate;
		this.exchange = exchange;
		this.objectMapper = objectMapper;
	}

	@Override
	@SneakyThrows
	public  <T, R> R handleRequest(String routingKey, T message, Class<R> returnType) {

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
				return objectMapper.convertValue(value, returnType);
			} catch (IllegalArgumentException ignored) {
				TypeAwareExceptionWrapper<?> exception = objectMapper.convertValue(value, TypeAwareExceptionWrapper.class);
				throw exception.getClazz().getDeclaredConstructor(String.class).newInstance(exception.getException().getMessage());
			}
		} catch (InterruptedException e) {
			throw new AmqpTimeoutException("Timeout reached");
		}
	}

}
