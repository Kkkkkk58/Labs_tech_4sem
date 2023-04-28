package ru.kslacker.cats.services.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;
import ru.kslacker.cats.services.validation.annotations.Password;
import ru.kslacker.cats.services.validation.annotations.ValidUserId;
import java.time.LocalDate;
import java.util.Optional;

// TODO object instead of optional
@Builder
public record UserUpdateModel(@ValidUserId Long id,
							  Optional<@Email String> email,
							  Optional<@Password String> password,
							  Boolean enabled,
							  Boolean locked,
							  Optional<@FutureOrPresent LocalDate> accountExpirationDate,
							  Optional<@FutureOrPresent LocalDate> credentialsExpirationDate) {


}
