package ru.kslacker.cats.microservices.catowners.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.microservices.common.exceptions.TypeAwareExceptionWrapper;
import ru.kslacker.cats.microservices.utils.json.PageableDeserializer;

@Configuration
public class AmqpConfiguration {

	@Value("${rabbitmq.exchange.name:kslacker.cats}")
	private String exchangeName;
	@Value("${rabbitmq.group.name:cat-owner}")
	private String amqpGroupName;
	@Value("${rabbitmq.owner-exists.queue.name:owner-exists-queue}")
	private String ownerExistsQueueName;
	@Value("${rabbitmq.owner-create.queue.name:owner-create-queue}")
	private String ownerCreateQueueName;
	// TODO implement the rest

	@Bean
	public Exchange exchange() {
		return new DirectExchange(exchangeName);
	}

	@Bean
	public Queue ownerExistsQueue() {
		return new Queue(ownerExistsQueueName);
	}

	@Bean
	public Queue createOwnerQueue() {
		return new Queue(ownerCreateQueueName);
	}

	@Bean
	public Queue deleteOwnerQueue() {
		return new Queue("owner-delete-queue");
	}

	@Bean
	public Queue getOwnerQueue() {
		return new Queue("owner-get-queue");
	}

	@Bean
	public Queue getOwnerByParamsQueue() {
		return new Queue("owner-get-by-queue");
	}

	@Bean
	public Queue updateOwnerQueue() {
		return new Queue("owner-update-queue");
	}

	@Bean
	public Declarables bindings() {
		return new Declarables(
			BindingBuilder.bind(ownerExistsQueue()).to(exchange()).with("owner.exists").noargs(),
			BindingBuilder.bind(createOwnerQueue()).to(exchange()).with("owner.create").noargs(),
			BindingBuilder.bind(deleteOwnerQueue()).to(exchange()).with("owner.delete").noargs(),
			BindingBuilder.bind(getOwnerQueue()).to(exchange()).with("owner.get").noargs(),
			BindingBuilder.bind(getOwnerByParamsQueue()).to(exchange()).with("owner.getby").noargs(),
			BindingBuilder.bind(updateOwnerQueue()).to(exchange()).with("owner.update").noargs()
		);
	}

	@Bean
	public String amqpGroupName() {
		return amqpGroupName;
	}

	@Bean
	@Primary
	public ObjectMapper advancedMapper() {

		SimpleModule module = new SimpleModule();
		module.addDeserializer(Pageable.class, new PageableDeserializer());

		return new ObjectMapper()
			.findAndRegisterModules()
			.registerModule(module);
	}

	@Bean
	public MessageConverter jackson2MessageConverter() {

		return new Jackson2JsonMessageConverter(advancedMapper());
	}

	@Bean
	@Primary
	public AmqpTemplate jsonRabbitTemplate(RabbitTemplate rabbitTemplate) {
		rabbitTemplate.setMessageConverter(jackson2MessageConverter());
		return rabbitTemplate;
	}

	@Configuration
	public static class RabbitErrorHandler implements RabbitListenerErrorHandler {

		@Override
		public Object handleError(Message amqpMessage,
			org.springframework.messaging.Message<?> message,
			ListenerExecutionFailedException exception) {

			return new TypeAwareExceptionWrapper<>(exception.getCause());
		}
	}

	@Bean
	public RabbitErrorHandler rabbitErrorHandler() {
		return new RabbitErrorHandler();
	}

}
