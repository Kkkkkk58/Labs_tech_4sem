package ru.kslacker.cats.microservices.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import lombok.Builder;

@Builder
public record Credentials(@NotBlank String username,
						  Optional<@Email String> email,
						  // TODO add @Password
						  String password) {

}
