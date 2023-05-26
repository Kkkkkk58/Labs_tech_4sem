package ru.kslacker.cats.microservices.users.dto;

import java.time.LocalDate;
import lombok.Builder;
import ru.kslacker.cats.microservices.users.validation.annotations.ValidUserId;

@Builder
public record UserUpdateInformation(@ValidUserId Long id,
									String email,
									String password,
									Boolean enabled,
									Boolean locked,
									LocalDate accountExpirationDate,
									LocalDate credentialsExpirationDate) {

}
