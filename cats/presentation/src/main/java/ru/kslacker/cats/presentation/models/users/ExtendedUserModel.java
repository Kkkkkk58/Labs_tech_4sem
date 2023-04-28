package ru.kslacker.cats.presentation.models.users;

import jakarta.validation.Valid;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.presentation.models.catowners.CatOwnerModel;
import ru.kslacker.cats.services.models.Credentials;

public record ExtendedUserModel(@Valid Credentials credentials,
								UserRole role,
								@Valid CatOwnerModel model) {

}
