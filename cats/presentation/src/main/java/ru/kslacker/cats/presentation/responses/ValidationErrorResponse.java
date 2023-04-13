package ru.kslacker.cats.presentation.responses;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) {

}
