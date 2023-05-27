package ru.kslacker.cats.microservices.users.dto;

import jakarta.validation.Valid;
import lombok.Builder;
import ru.kslacker.cats.microservices.jpaentities.models.UserRole;

@Builder
public record UserCreateDto(@Valid Credentials credentials,
							UserRole userRole,
							@Valid CatOwnerInformation catOwnerInformation) {

}
