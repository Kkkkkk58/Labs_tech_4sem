package ru.kslacker.cats.presentation.models.catowners;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import ru.kslacker.cats.services.validation.ValidUpdateName;
import ru.kslacker.cats.presentation.validation.ValidationGroup;
import java.time.LocalDate;

public record CatOwnerModel(@NotBlank(groups = ValidationGroup.OnCreate.class)
							@ValidUpdateName(groups = ValidationGroup.OnUpdate.class)
							String name,

							@PastOrPresent(groups = ValidationGroup.OnCreate.class)
							LocalDate dateOfBirth) {

}
