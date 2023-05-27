package ru.kslacker.cats.microservices.restapi.models.inherited;

import lombok.Builder;

@Builder
public record FriendsInfo(Long cat1Id, Long cat2Id) {

}
