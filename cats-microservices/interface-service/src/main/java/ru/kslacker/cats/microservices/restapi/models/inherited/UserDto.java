package ru.kslacker.cats.microservices.restapi.models.inherited;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserDto(Long id,
					  String username,
					  String email,
					  Long ownerId,
					  boolean enabled,
					  boolean locked,
					  LocalDate accountExpirationDate,
					  LocalDate credentialsExpirationDate) {

}
