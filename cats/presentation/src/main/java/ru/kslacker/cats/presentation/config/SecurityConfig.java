package ru.kslacker.cats.presentation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//TODO @SecurityScheme(type = SecuritySchemeType.HTTP)
public class SecurityConfig {

	private final UserDetailsService userDetailsService;

	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());

		return authenticationProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf().disable()
			.cors().disable()
			.authorizeHttpRequests()
			.requestMatchers("/auth/**").permitAll()
			.requestMatchers("/", "/index.html", "/css/**").permitAll()
			.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
			.anyRequest().authenticated()
			.and()
//			.formLogin().permitAll()
//			.and().logout().permitAll().and()
//			.httpBasic()
//			.authenticationEntryPoint((request, response, authException) -> response.sendError(
//				HttpServletResponse.SC_UNAUTHORIZED))
//			.and()
			.authenticationProvider(authenticationProvider())
			.build();
//		http.csrf().disable().cors().disable();
//		http.authorizeHttpRequests().
//			requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN").anyRequest().authenticated()
//			.and().formLogin().defaultSuccessUrl("/swagger-ui/index.html?continue&continue#/").permitAll().and().logout().permitAll().logoutSuccessUrl("/");
//		return http.build();
	}

}
