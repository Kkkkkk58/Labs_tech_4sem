package ru.kslacker.cats.services.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserDto(Long id,
					  String username,
					  String email,
					  CatOwnerDto owner,
					  boolean enabled,
					  boolean locked,
					  LocalDate accountExpirationDate,
					  LocalDate credentialsExpirationDate) {

}
