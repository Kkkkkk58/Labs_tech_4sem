package ru.kslacker.cats.presentation.models.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import ru.kslacker.cats.presentation.models.catowners.CatOwnerModel;
import ru.kslacker.cats.presentation.validation.ValidationGroup;
import ru.kslacker.cats.services.validation.Password;
import java.util.Optional;

@Validated
public record CreateUserModel(@NotBlank String username, Optional<@Email String> email, @Password String password, @Validated(ValidationGroup.OnCreate.class) CatOwnerModel catOwnerModel) {

}
