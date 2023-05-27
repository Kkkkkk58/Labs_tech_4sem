package ru.kslacker.cats.microservices.users.dto;

import lombok.Builder;

import java.time.LocalDate;

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
