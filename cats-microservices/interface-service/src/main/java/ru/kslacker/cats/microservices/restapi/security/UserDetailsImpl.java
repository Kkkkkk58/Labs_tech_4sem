package ru.kslacker.cats.microservices.restapi.security;

import java.time.LocalDate;
import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kslacker.cats.microservices.jpaentities.models.UserRole;

@Builder
@Data
public class UserDetailsImpl implements UserDetails {

	private final Collection<UserRole> authorities;
	private final String password;
	private final String username;
	private final LocalDate accountExpirationDate;
	private final boolean isLocked;
	private final LocalDate credentialsExpirationDate;
	private final boolean isEnabled;
	private final String email;
	private final Long id;
	private final Long ownerId;

	@Override
	public boolean isAccountNonExpired() {
		return isNonExpired(accountExpirationDate);
	}

	@Override
	public boolean isAccountNonLocked() {
		return !isLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isNonExpired(credentialsExpirationDate);
	}

	public boolean isAdmin() {
		return getAuthorities().contains(UserRole.ADMIN);
	}

	public boolean isUser() {
		return getAuthorities().contains(UserRole.USER);
	}

	private boolean isNonExpired(LocalDate expirationDate) {
		LocalDate currentDate = LocalDate.now();
		return expirationDate == null
			|| currentDate.equals(expirationDate)
			|| currentDate.isBefore(expirationDate);
	}
}
