package ru.kslacker.cats.presentation.dto;

import java.util.List;

public record CatOwnerDto(Long id, String name, List<CatDto> cats) {

}
