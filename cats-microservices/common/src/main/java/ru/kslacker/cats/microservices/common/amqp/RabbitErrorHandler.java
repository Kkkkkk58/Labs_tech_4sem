package ru.kslacker.cats.microservices.common.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;

public class RabbitErrorHandler implements RabbitListenerErrorHandler {

	@Override
	public Object handleError(Message amqpMessage,
		org.springframework.messaging.Message<?> message,
		ListenerExecutionFailedException exception) {

		return new TypeAwareExceptionWrapper<>(exception.getCause());
	}
}
