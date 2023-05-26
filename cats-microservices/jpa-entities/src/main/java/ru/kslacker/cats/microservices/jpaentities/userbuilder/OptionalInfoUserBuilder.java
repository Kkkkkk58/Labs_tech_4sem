package ru.kslacker.cats.microservices.jpaentities.userbuilder;

import java.time.LocalDate;
import ru.kslacker.cats.microservices.jpaentities.entities.User;
import ru.kslacker.cats.microservices.jpaentities.models.UserRole;

public interface OptionalInfoUserBuilder {

	OptionalInfoUserBuilder withEmail(String email);

	OptionalInfoUserBuilder withOwnerId(Long catOwnerId);

	OptionalInfoUserBuilder withRole(UserRole role);

	OptionalInfoUserBuilder withAccountExpirationDate(LocalDate accountExpirationDate);

	OptionalInfoUserBuilder withCredentialsExpirationDate(LocalDate credentialsExpirationDate);

	OptionalInfoUserBuilder isEnabled(boolean enabled);

	OptionalInfoUserBuilder isLocked(boolean locked);

	User build();
}
