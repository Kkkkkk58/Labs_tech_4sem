package ru.kslacker.cats.presentation.models.cats;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.presentation.validation.ValidationGroup;
import ru.kslacker.cats.services.validation.annotations.PastOrPresentUpdateDate;
import ru.kslacker.cats.services.validation.annotations.UpdateName;

public record CatModel(
	@NotBlank(groups = ValidationGroup.OnCreate.class)
	@UpdateName(groups = ValidationGroup.OnUpdate.class)
	String name,
	@PastOrPresent(groups = ValidationGroup.OnCreate.class)
	@PastOrPresentUpdateDate(groups = ValidationGroup.OnUpdate.class)
	LocalDate dateOfBirth,
	@NotBlank(groups = ValidationGroup.OnCreate.class)
	String breed,
	FurColor furColor,

	@Positive(groups = ValidationGroup.OnCreate.class)
	@Null(groups = ValidationGroup.OnUpdate.class)
	@JsonIgnore
	Long ownerId) {

}
