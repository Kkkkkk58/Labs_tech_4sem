package ru.kslacker.cats.services.models.users;

import java.time.LocalDate;
import lombok.Builder;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.common.models.UserRole;

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
