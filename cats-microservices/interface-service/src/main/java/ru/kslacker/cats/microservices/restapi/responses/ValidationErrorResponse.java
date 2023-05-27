package ru.kslacker.cats.microservices.restapi.responses;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {

}
