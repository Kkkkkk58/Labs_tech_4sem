package ru.kslacker.cats.microservices.users.dto;

import lombok.Builder;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.microservices.jpaentities.models.UserRole;
import java.time.LocalDate;

@Builder
public record UserSearchOptions(String username,
								String email,
								UserRole role,
								Boolean locked,
								Boolean enabled,
								LocalDate accountExpirationDate,
								LocalDate credentialsExpirationDate,
								Pageable pageable) {

}
