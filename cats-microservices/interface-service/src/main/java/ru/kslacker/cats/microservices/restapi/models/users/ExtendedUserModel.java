package ru.kslacker.cats.microservices.restapi.models.users;

import jakarta.validation.Valid;
import ru.kslacker.cats.microservices.jpaentities.models.UserRole;
import ru.kslacker.cats.microservices.restapi.models.catowners.CatOwnerModel;
import ru.kslacker.cats.microservices.restapi.models.inherited.Credentials;

public record ExtendedUserModel(@Valid Credentials credentials,
								@Valid CatOwnerModel catOwnerModel,
								UserRole role) {

}
