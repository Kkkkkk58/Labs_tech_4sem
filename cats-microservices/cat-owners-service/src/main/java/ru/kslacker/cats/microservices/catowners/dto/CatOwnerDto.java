package ru.kslacker.cats.microservices.catowners.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record CatOwnerDto(Long id,
                          String name,
                          LocalDate dateOfBirth,
                          List<Long> cats) {

}
