package ru.kslacker.cats.presentation.models.cats;

import ru.kslacker.cats.common.models.FurColor;
import java.time.LocalDate;

public record GetCatByParamsModel(Long id, String name, LocalDate dateOfBirth, String breed, FurColor furColor, Long owner_id) {

}
