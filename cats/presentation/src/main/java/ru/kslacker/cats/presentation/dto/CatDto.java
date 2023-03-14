package ru.kslacker.cats.presentation.dto;

import java.time.LocalDate;
import java.util.List;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.models.Breed;
import ru.kslacker.cats.dataaccess.models.FurColor;

public record CatDto(Long id, String name, LocalDate dateOfBirth, Breed breed, FurColor furColor, CatOwner owner, List<CatDto> friends) {

}
