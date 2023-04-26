package ru.kslacker.cats.presentation.models.catowners;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import ru.kslacker.cats.presentation.validation.ValidationGroup;
import ru.kslacker.cats.services.validation.UpdateDate;
import ru.kslacker.cats.services.validation.UpdateName;

public record CatOwnerModel(@NotBlank(groups = ValidationGroup.OnCreate.class)
							@UpdateName(groups = ValidationGroup.OnUpdate.class)
							String name,

							@PastOrPresent(groups = ValidationGroup.OnCreate.class)
							@UpdateDate(groups = ValidationGroup.OnUpdate.class)
							LocalDate dateOfBirth) {

}
