package ru.kslacker.cats.microservices.users.dto;

import java.time.LocalDate;
import java.util.Collection;
import lombok.Builder;
import ru.kslacker.cats.microservices.jpaentities.models.UserRole;

@Builder
public record UserDetails(
	Collection<UserRole> authorities,
	String password,
	String username,
	LocalDate accountExpirationDate,
	boolean isLocked,
	LocalDate credentialsExpirationDate,
	boolean isEnabled,
	String email,
	Long id,
	Long ownerId) {

}
