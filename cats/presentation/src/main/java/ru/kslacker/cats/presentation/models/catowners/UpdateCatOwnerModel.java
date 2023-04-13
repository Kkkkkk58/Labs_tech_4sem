package ru.kslacker.cats.presentation.models.catowners;

import java.time.LocalDate;
import java.util.List;

public record UpdateCatOwnerModel(String name, LocalDate dateOfBirth, List<Long> cats) {

}
