package ru.kslacker.cats.presentation.models.cats;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.services.validation.ValidUpdateName;
import ru.kslacker.cats.presentation.validation.ValidationGroup;

public record CatModel(
	@NotBlank(groups = ValidationGroup.OnCreate.class)
	@ValidUpdateName(groups = ValidationGroup.OnUpdate.class)
	String name,
	@PastOrPresent(groups = ValidationGroup.OnCreate.class)
	LocalDate dateOfBirth,
	String breed,
	FurColor furColor,
	@Positive
	Long ownerId) {

}
