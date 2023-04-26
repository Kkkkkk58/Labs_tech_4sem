package ru.kslacker.cats.presentation.models.cats;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.presentation.validation.ValidationGroup;
import ru.kslacker.cats.services.validation.UpdateDate;
import ru.kslacker.cats.services.validation.UpdateName;

public record CatModel(
	@NotBlank(groups = ValidationGroup.OnCreate.class)
	@UpdateName(groups = ValidationGroup.OnUpdate.class)
	String name,
	@PastOrPresent(groups = ValidationGroup.OnCreate.class)
	@UpdateDate(groups = ValidationGroup.OnUpdate.class)
	LocalDate dateOfBirth,
	String breed,
	FurColor furColor,
	@Positive(groups = ValidationGroup.OnCreate.class)
	@Null(groups = ValidationGroup.OnUpdate.class)
	Long ownerId) {

}
