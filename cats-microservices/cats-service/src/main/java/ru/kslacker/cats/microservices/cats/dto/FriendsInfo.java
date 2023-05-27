package ru.kslacker.cats.microservices.cats.dto;

import lombok.Builder;

@Builder
public record FriendsInfo(Long cat1Id, Long cat2Id) {

}
