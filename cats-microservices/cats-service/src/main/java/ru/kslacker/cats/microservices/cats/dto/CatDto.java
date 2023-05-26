package ru.kslacker.cats.microservices.cats.dto;

import lombok.Builder;
import ru.kslacker.cats.microservices.jpaentities.models.FurColor;


import java.time.LocalDate;
import java.util.List;

@Builder
public record CatDto(Long id,
                     String name,
                     LocalDate dateOfBirth,
                     String breed,
                     FurColor furColor,
                     Long ownerId,
                     List<Long> friends) {

}
