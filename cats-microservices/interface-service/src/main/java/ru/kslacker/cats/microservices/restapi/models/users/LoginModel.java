package ru.kslacker.cats.microservices.restapi.models.users;

import jakarta.validation.constraints.NotBlank;
import ru.kslacker.cats.microservices.restapi.validation.annotations.Password;

public record LoginModel(@NotBlank String username, @Password String password) {

}
