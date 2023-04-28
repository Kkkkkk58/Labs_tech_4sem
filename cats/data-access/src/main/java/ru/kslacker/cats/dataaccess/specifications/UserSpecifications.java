package ru.kslacker.cats.dataaccess.specifications;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.entities.UserAccount;

public class UserSpecifications {

	public static Specification<UserAccount> withUsername(String username) {
		if (username == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"),
			username));
	}

	public static Specification<UserAccount> withEmail(String email) {
		if (email == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email));
	}

	public static Specification<UserAccount> withRole(UserRole role) {
		if (role == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role));
	}

	public static Specification<UserAccount> withLock(Boolean locked) {
		if (locked == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("locked"),
			locked));
	}

	public static Specification<UserAccount> withStatus(Boolean enabled) {
		if (enabled == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"),
			enabled));
	}

	public static Specification<UserAccount> withAccountExpirationDate(
		LocalDate accountExpiryDate) {
		if (accountExpiryDate == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(
			root.get("accountExpirationDate"), accountExpiryDate));
	}

	public static Specification<UserAccount> withCredentialsExpirationDate(
		LocalDate credentialsExpirationDate) {
		if (credentialsExpirationDate == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(
			root.get("credentialsExpirationDate"), credentialsExpirationDate));
	}
}
