package ru.kslacker.cats.microservices.restapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.microservices.restapi.services.api.AmqpRelyingService;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl extends AmqpRelyingService implements UserDetailsService {


	protected UserDetailsServiceImpl(
		AsyncAmqpTemplate asyncAmqpTemplate,
		Exchange exchange,
		ObjectMapper objectMapper) {

		super(asyncAmqpTemplate, exchange, objectMapper);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return handleRequest("user.getby.username", username, UserDetailsImpl.class);
	}
}
