package ru.kslacker.cats.presentation.models.catowners;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record CreateCatOwnerModel(@NotBlank String name,
								  @PastOrPresent LocalDate dateOfBirth) {

}
