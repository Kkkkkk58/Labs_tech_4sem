package ru.kslacker.cats.microservices.restapi.models.inherited;

import java.time.LocalDate;
import lombok.Builder;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.microservices.jpaentities.models.UserRole;

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
