package ru.kslacker.cats.presentation.models.catowners;

import java.time.LocalDate;

public record GetCatOwnerByParamsModel(Long id, String name, LocalDate dateOfBirth) {

}
