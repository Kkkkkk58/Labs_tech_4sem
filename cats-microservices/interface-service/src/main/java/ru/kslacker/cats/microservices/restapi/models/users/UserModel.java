package ru.kslacker.cats.microservices.restapi.models.users;

import jakarta.validation.Valid;
import ru.kslacker.cats.microservices.restapi.models.catowners.CatOwnerModel;
import ru.kslacker.cats.microservices.restapi.models.inherited.Credentials;

public record UserModel(
	@Valid Credentials credentials,
	@Valid CatOwnerModel catOwnerModel) {


}
