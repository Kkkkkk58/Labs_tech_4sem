package ru.kslacker.cats.microservices.restapi.models.users;

import java.time.LocalDate;

public record UserUpdateModel(String email,
							  String password,
							  Boolean enabled,
							  Boolean locked,
							  LocalDate accountExpirationDate,
							  LocalDate credentialsExpirationDate) {

}
