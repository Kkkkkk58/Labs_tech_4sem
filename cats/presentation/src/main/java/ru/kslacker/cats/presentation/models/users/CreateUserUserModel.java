package ru.kslacker.cats.presentation.models.users;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import ru.kslacker.cats.presentation.models.catowners.CatOwnerModel;
import ru.kslacker.cats.services.validation.annotations.Password;

public record CreateUserUserModel(
	@NotBlank
	String username,
	Optional<@Email String> email,
	@Password
	String password,
	@Valid
	CatOwnerModel catOwnerModel) {


}
