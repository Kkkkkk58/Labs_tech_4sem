package ru.kslacker.cats.services.security;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.User;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Set.of(user.getRole());
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return isNonExpired(user.getAccountExpirationDate());
	}

	@Override
	public boolean isAccountNonLocked() {
		return !user.isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isNonExpired(user.getCredentialsExpirationDate());
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public Long getId() {
		return user.getId();
	}

	public Long getOwnerId() {
		CatOwner owner = user.getOwner();
		return (owner == null) ? null : owner.getId();
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
