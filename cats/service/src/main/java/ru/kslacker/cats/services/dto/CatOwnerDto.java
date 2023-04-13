package ru.kslacker.cats.services.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record CatOwnerDto(Long id,
						  String name,
						  LocalDate dateOfBirth,
						  List<Long> cats) {

}
