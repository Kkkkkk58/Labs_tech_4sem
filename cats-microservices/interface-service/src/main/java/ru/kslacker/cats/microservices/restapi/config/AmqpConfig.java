package ru.kslacker.cats.microservices.restapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
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
import ru.kslacker.cats.microservices.common.amqp.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.utils.json.PageableDeserializer;

@Configuration
public class AmqpConfig {

	@Value("${rabbitmq.exchange.name:kslacker.cats}")
	private String exchangeName;


	@Bean
	public Exchange exchange() {
		return new DirectExchange(exchangeName);
	}

	@Bean
	@Primary
	public ObjectMapper advancedMapper() {

		SimpleModule module = new SimpleModule();
		module.addDeserializer(Pageable.class, new PageableDeserializer());

		return new ObjectMapper()
			.findAndRegisterModules()
			.registerModule(module)
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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

}
