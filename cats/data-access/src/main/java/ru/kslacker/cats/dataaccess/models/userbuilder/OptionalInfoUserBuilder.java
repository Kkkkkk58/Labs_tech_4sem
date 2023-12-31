package ru.kslacker.cats.dataaccess.models.userbuilder;

import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.User;
import java.time.LocalDate;

public interface OptionalInfoUserBuilder {

	OptionalInfoUserBuilder withEmail(String email);

	OptionalInfoUserBuilder withOwner(CatOwner catOwner);

	OptionalInfoUserBuilder withRole(UserRole role);

	OptionalInfoUserBuilder withAccountExpirationDate(LocalDate accountExpirationDate);

	OptionalInfoUserBuilder withCredentialsExpirationDate(LocalDate credentialsExpirationDate);

	OptionalInfoUserBuilder isEnabled(boolean enabled);

	OptionalInfoUserBuilder isLocked(boolean locked);

	User build();
}
