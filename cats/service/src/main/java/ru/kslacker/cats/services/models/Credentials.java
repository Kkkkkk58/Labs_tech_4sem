package ru.kslacker.cats.services.models;

import lombok.Builder;

@Builder
public record Credentials(String username, String email, String password) {

}
