package ru.kslacker.cats.microservices.restapi.models.inherited;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import ru.kslacker.cats.microservices.restapi.validation.annotations.Password;

import java.util.Optional;

@Builder
public record Credentials(@NotBlank String username,
                          Optional<@Email String> email,
                          @Password String password) {

}
