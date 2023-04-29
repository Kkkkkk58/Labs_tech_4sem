package ru.kslacker.cats.services.models.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import lombok.Builder;
import ru.kslacker.cats.services.validation.annotations.Password;

@Builder
public record Credentials(@NotBlank String username,
						  Optional<@Email String> email,
						  @Password String password) {

}
