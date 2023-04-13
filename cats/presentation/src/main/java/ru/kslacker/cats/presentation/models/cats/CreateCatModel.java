package ru.kslacker.cats.presentation.models.cats;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import ru.kslacker.cats.common.models.FurColor;

public record CreateCatModel(
	@NotBlank String name,
	@PastOrPresent LocalDate dateOfBirth,
	String breed,
	FurColor furColor,
	@Positive Long ownerId) {

}
