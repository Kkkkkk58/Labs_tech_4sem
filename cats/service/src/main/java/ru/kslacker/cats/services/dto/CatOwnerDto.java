package ru.kslacker.cats.services.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record CatOwnerDto(Long id, String name, List<CatDto> cats) {

}
