package ru.kslacker.cats.services.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import ru.kslacker.cats.common.models.FurColor;

// TODO Builder
@Builder
public record CatDto(Long id, String name, LocalDate dateOfBirth, String breed, FurColor furColor, CatOwnerDto owner, List<CatDto> friends) {

}
