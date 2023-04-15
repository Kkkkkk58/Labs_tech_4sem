package ru.kslacker.cats.presentation.models.catowners;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import ru.kslacker.cats.presentation.validation.ValidationGroup;
import ru.kslacker.cats.services.validation.ValidUpdateName;

public record CatOwnerModel(@NotBlank(groups = ValidationGroup.OnCreate.class)
							@ValidUpdateName(groups = ValidationGroup.OnUpdate.class)
							String name,

							@PastOrPresent(groups = ValidationGroup.OnCreate.class)
							LocalDate dateOfBirth) {

}
