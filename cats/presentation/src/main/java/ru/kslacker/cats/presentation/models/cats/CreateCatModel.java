package ru.kslacker.cats.presentation.models.cats;

import ru.kslacker.cats.common.models.FurColor;

import java.time.LocalDate;

public record CreateCatModel(String name, LocalDate dateOfBirth, String breed, FurColor furColor,
							 Long ownerId) {

}
