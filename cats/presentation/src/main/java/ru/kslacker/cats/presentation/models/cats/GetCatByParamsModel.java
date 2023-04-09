package ru.kslacker.cats.presentation.models.cats;

import java.time.LocalDate;
import ru.kslacker.cats.common.models.FurColor;

public record GetCatByParamsModel(Long id,
								  String name,
								  LocalDate dateOfBirth,
								  String breed,
								  FurColor furColor,
								  Long owner_id) {

}
