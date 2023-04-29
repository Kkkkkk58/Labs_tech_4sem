package ru.kslacker.cats.presentation.models.users;

import jakarta.validation.Valid;
import ru.kslacker.cats.presentation.models.catowners.CatOwnerModel;
import ru.kslacker.cats.services.models.users.Credentials;

public record UserModel(
	@Valid Credentials credentials,
	@Valid CatOwnerModel catOwnerModel) {


}
