package ru.kslacker.cats.presentation.models.users;

import jakarta.validation.constraints.NotBlank;
import ru.kslacker.cats.services.validation.annotations.Password;

public record LoginModel(@NotBlank String username, @Password String password) {

}
