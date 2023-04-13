package ru.kslacker.cats.presentation.models.cats;

import ru.kslacker.cats.common.models.FurColor;
import java.time.LocalDate;
import java.util.List;

public record UpdateCatModel(String name, LocalDate dateOfBirth, String breed, FurColor furColor, Long ownerId, List<Long> friends) {

}
