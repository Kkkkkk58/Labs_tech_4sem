package ru.kslacker.cats.services.models.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.util.Optional;
import lombok.Builder;
import ru.kslacker.cats.services.validation.annotations.Password;
import ru.kslacker.cats.services.validation.annotations.ValidUserId;

@Builder
public record UserUpdateInformation(@ValidUserId Long id,
									String email,
									String password,
									Boolean enabled,
									Boolean locked,
									LocalDate accountExpirationDate,
									LocalDate credentialsExpirationDate) {

}
