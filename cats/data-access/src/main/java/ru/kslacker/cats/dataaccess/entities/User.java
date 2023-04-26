package ru.kslacker.cats.dataaccess.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.models.OptionalInfoUserBuilder;
import ru.kslacker.cats.dataaccess.models.PasswordUserBuilder;
import ru.kslacker.cats.dataaccess.models.UsernameUserBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Setter(AccessLevel.NONE)
	private Long id;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "email")
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "owner_id")
	@ToString.Exclude
	private CatOwner owner;

	@Column(name = "role", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole role = UserRole.USER;

	@Column(name = "enabled", nullable = false)
	private boolean enabled = true;

	@Column(name = "locked", nullable = false)
	private boolean locked = false;

	@Column(name = "account_expiration_date")
	private LocalDate accountExpirationDate = null;

	@Column(name = "credentials_expiration_date")
	private LocalDate credentialsExpirationDate = null;

	public User(String username, String email, String password, CatOwner owner, UserRole role,
		boolean enabled, boolean locked, LocalDate accountExpirationDate,
		LocalDate credentialsExpirationDate) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.owner = owner;
		this.role = role;
		this.enabled = enabled;
		this.locked = locked;
		this.accountExpirationDate = accountExpirationDate;
		this.credentialsExpirationDate = credentialsExpirationDate;
	}

	public static UserBuilder builder() {
		return new UserBuilder();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		User user = (User) o;
		return id != null && Objects.equals(id, user.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	public static class UserBuilder implements UsernameUserBuilder, PasswordUserBuilder,
		OptionalInfoUserBuilder {

		private String username;
		private String password;
		private String email = null;
		private CatOwner owner = null;
		private UserRole role = UserRole.USER;
		private boolean enabled = true;
		private boolean locked = false;
		private LocalDate accountExpirationDate = null;
		private LocalDate credentialsExpirationDate = null;

		@Override
		public OptionalInfoUserBuilder withPassword(String password) {
			this.password = password;
			return this;
		}

		@Override
		public PasswordUserBuilder withUsername(String username) {
			this.username = username;
			return this;
		}

		@Override
		public OptionalInfoUserBuilder withEmail(String email) {
			this.email = email;
			return this;
		}

		@Override
		public OptionalInfoUserBuilder withOwner(CatOwner catOwner) {
			this.owner = catOwner;
			return this;
		}

		@Override
		public OptionalInfoUserBuilder withRole(UserRole role) {
			this.role = role;
			return this;
		}

		@Override
		public OptionalInfoUserBuilder withAccountExpirationDate(LocalDate accountExpirationDate) {
			this.accountExpirationDate = accountExpirationDate;
			return this;
		}

		@Override
		public OptionalInfoUserBuilder withCredentialsExpirationDate(
			LocalDate credentialsExpirationDate) {
			this.credentialsExpirationDate = credentialsExpirationDate;
			return this;
		}

		@Override
		public OptionalInfoUserBuilder isEnabled(boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		@Override
		public OptionalInfoUserBuilder isLocked(boolean locked) {
			this.locked = locked;
			return this;
		}

		@Override
		public User build() {
			if (username == null || password == null) {
				throw new RuntimeException(); // TODO
			}

			return new User(username, email, password, owner, role, enabled, locked, accountExpirationDate, credentialsExpirationDate);
		}
	}
}
