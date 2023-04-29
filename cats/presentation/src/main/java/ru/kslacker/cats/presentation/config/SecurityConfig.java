package ru.kslacker.cats.presentation.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler;

	@Autowired
	public SecurityConfig(SavedRequestAwareAuthenticationSuccessHandler loginSuccessHandler) {
		this.loginSuccessHandler = loginSuccessHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf().disable()
			.cors().disable()
			.authorizeHttpRequests()
			.requestMatchers("/api/v3/auth/register").permitAll()
			.requestMatchers("/", "/index.html", "/resources/**", "/assets/**").permitAll()
			.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
			.anyRequest()
			.authenticated()                                    // TODO + mb update views
			.and().formLogin().loginPage("/login")
			.defaultSuccessUrl("/").permitAll()
			.and().logout().logoutSuccessUrl("/").permitAll()
			.and().httpBasic()
			.authenticationEntryPoint((request, response, authException) -> response.sendError(
				HttpServletResponse.SC_UNAUTHORIZED))
			.and()
			.build();
	}

}
