package ru.kslacker.cats.microservices.cats.config;

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
	@Value("${rabbitmq.group.name:cat}")
	private String amqpGroupName;
	@Value("${rabbitmq.owner-exists.queue.name:cat-exists-queue}")
	private String catExistsQueueName;
	@Value("${rabbitmq.owner-create.queue.name:cat-create-queue}")
	private String catCreateQueueName;
	// TODO implement the rest

	@Bean
	public Exchange exchange() {
		return new DirectExchange(exchangeName);
	}

	@Bean
	public Queue catExistsQueue() {
		return new Queue(catExistsQueueName);
	}

	@Bean
	public Queue createCatQueue() {
		return new Queue(catCreateQueueName);
	}

	@Bean
	public Queue deleteCatQueue() {
		return new Queue("cat-delete-queue");
	}

	@Bean
	public Queue getCatQueue() {
		return new Queue("cat-get-queue");
	}

	@Bean
	public Queue getCatByParamsQueue() {
		return new Queue("cat-get-by-queue");
	}

	@Bean
	public Queue updateCatQueue() {
		return new Queue("cat-update-queue");
	}

	@Bean
	public Queue addFriendQueue() {
		return new Queue("cat-add-friend-queue");
	}

	@Bean
	public Queue deleteFriendQueue() {
		return new Queue("cat-delete-friend-queue");
	}

	@Bean
	public Declarables bindings() {
		return new Declarables(
			BindingBuilder.bind(catExistsQueue()).to(exchange()).with("cat.exists").noargs(),
			BindingBuilder.bind(createCatQueue()).to(exchange()).with("cat.create").noargs(),
			BindingBuilder.bind(deleteCatQueue()).to(exchange()).with("cat.delete").noargs(),
			BindingBuilder.bind(getCatQueue()).to(exchange()).with("cat.get").noargs(),
			BindingBuilder.bind(getCatByParamsQueue()).to(exchange()).with("cat.getby").noargs(),
			BindingBuilder.bind(updateCatQueue()).to(exchange()).with("cat.update").noargs(),
			BindingBuilder.bind(addFriendQueue()).to(exchange()).with("cat.friend.add").noargs(),
			BindingBuilder.bind(deleteFriendQueue()).to(exchange()).with("cat.friend.delete").noargs()
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
