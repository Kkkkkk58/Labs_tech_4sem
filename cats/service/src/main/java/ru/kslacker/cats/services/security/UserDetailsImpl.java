package ru.kslacker.cats.services.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.UserAccount;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private final UserAccount userAccount;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Set.of(userAccount.getRole());
	}

	@Override
	public String getPassword() {
		return userAccount.getPassword();
	}

	@Override
	public String getUsername() {
		return userAccount.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return isNonExpired(userAccount.getAccountExpirationDate());
	}

	@Override
	public boolean isAccountNonLocked() {
		return !userAccount.isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isNonExpired(userAccount.getCredentialsExpirationDate());
	}

	@Override
	public boolean isEnabled() {
		return userAccount.isEnabled();
	}

	public String getEmail() {
		return userAccount.getEmail();
	}

	public Long getId() {
		return userAccount.getId();
	}

	public Long getOwnerId() {
		CatOwner owner = userAccount.getOwner();
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
		return expirationDate == null || currentDate.equals(expirationDate) || currentDate.isBefore(expirationDate);
	}
}
