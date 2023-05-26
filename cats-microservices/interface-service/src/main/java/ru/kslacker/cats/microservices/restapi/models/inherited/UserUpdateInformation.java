package ru.kslacker.cats.microservices.restapi.models.inherited;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UserUpdateInformation(Long id,
									String email,
									String password,
									Boolean enabled,
									Boolean locked,
									LocalDate accountExpirationDate,
									LocalDate credentialsExpirationDate) {

}
