package ru.kslacker.cats.microservices.restapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.microservices.common.amqp.api.AmqpRelyingService;

@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

	private final AmqpRelyingService amqpService;

	@Autowired
	protected UserDetailsServiceImpl(AmqpRelyingService amqpService) {
		this.amqpService = amqpService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return amqpService.handleRequest("user.getby.username", username, UserDetailsImpl.class);
	}
}
