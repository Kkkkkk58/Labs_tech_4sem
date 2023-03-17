package ru.kslacker.cats.presentation.models.catowners;

import java.time.LocalDate;

public record CreateCatOwnerModel(String name, LocalDate dateOfBirth) {

}
