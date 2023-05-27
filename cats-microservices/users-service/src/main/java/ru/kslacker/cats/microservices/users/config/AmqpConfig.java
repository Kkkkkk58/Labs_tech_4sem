package ru.kslacker.cats.microservices.users.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.microservices.common.amqp.AmqpRelyingServiceImpl;
import ru.kslacker.cats.microservices.common.amqp.RabbitErrorHandler;
import ru.kslacker.cats.microservices.common.amqp.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.utils.json.PageableDeserializer;

@Configuration
public class AmqpConfig {

	@Value("${rabbitmq.exchange.name:kslacker.cats}")
	private String exchangeName;
	@Value("${rabbitmq.group.name:cat-owner}")
	private String amqpGroupName;

	@Bean
	public Exchange exchange() {
		return new DirectExchange(exchangeName);
	}


	@Bean
	public String amqpGroupName() {
		return amqpGroupName;
	}

	@Bean
	public Queue createUserQueue() {
		return new Queue("user-create-queue");
	}

	@Bean
	public Queue getUserQueue() {
		return new Queue("user-get-queue");
	}

	@Bean
	public Queue getUserByParamsQueue() {
		return new Queue("user-get-by-queue");
	}

	@Bean
	public Queue deleteUserQueue() {
		return new Queue("user-delete-queue");
	}

	@Bean
	public Queue disableUserQueue() {
		return new Queue("user-disable-queue");
	}

	@Bean
	public Queue enableUserQueue() {
		return new Queue("user-enable-queue");
	}

	@Bean
	public Queue banUserQueue() {
		return new Queue("user-ban-queue");
	}

	@Bean
	public Queue unbanUserQueue() {
		return new Queue("user-unban-queue");
	}

	@Bean
	public Queue updateUserQueue() {
		return new Queue("user-update-queue");
	}

	@Bean
	public Queue promoteUserToAdminQueue() {
		return new Queue("user-promote-queue");
	}

	@Bean
	public Queue getUserByUsernameQueue() {
		return new Queue("user-getbyusername-queue");
	}

	@Bean
	public Declarables bindings() {
		return new Declarables(
			BindingBuilder.bind(createUserQueue()).to(exchange()).with("user.create").noargs(),
			BindingBuilder.bind(getUserQueue()).to(exchange()).with("user.get").noargs(),
			BindingBuilder.bind(getUserByParamsQueue()).to(exchange()).with("user.getby").noargs(),
			BindingBuilder.bind(deleteUserQueue()).to(exchange()).with("user.delete").noargs(),
			BindingBuilder.bind(disableUserQueue()).to(exchange()).with("user.enabled.false").noargs(),
			BindingBuilder.bind(enableUserQueue()).to(exchange()).with("user.enabled.true").noargs(),
			BindingBuilder.bind(banUserQueue()).to(exchange()).with("user.ban.true").noargs(),
			BindingBuilder.bind(unbanUserQueue()).to(exchange()).with("user.ban.false").noargs(),
			BindingBuilder.bind(updateUserQueue()).to(exchange()).with("user.update").noargs(),
			BindingBuilder.bind(promoteUserToAdminQueue()).to(exchange()).with("user.promote").noargs(),
			BindingBuilder.bind(getUserByUsernameQueue()).to(exchange()).with("user.getby.username").noargs()
		);
	}

	@Bean
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
	public RabbitTemplate jsonRabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jackson2MessageConverter());
		return rabbitTemplate;
	}

	@Bean
	@Primary
	public AsyncAmqpTemplate jsonRabbitAsyncTemplate(ConnectionFactory connectionFactory) {

		return new AsyncRabbitTemplate(jsonRabbitTemplate(connectionFactory));
	}

	@Bean
	public AmqpRelyingService amqpService(ConnectionFactory connectionFactory) {
		return new AmqpRelyingServiceImpl(jsonRabbitAsyncTemplate(connectionFactory), exchange(), advancedMapper());
	}

	@Bean
	public RabbitErrorHandler rabbitErrorHandler() {
		return new RabbitErrorHandler();
	}
}

