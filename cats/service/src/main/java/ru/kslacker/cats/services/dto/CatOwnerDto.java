package ru.kslacker.cats.services.dto;

import lombok.Builder;
import ru.kslacker.cats.services.validation.ValidCatOwnerDto;
import java.time.LocalDate;
import java.util.List;

@ValidCatOwnerDto
@Builder
public record CatOwnerDto(Long id, String name, LocalDate dateOfBirth, List<Long> cats) {

}
