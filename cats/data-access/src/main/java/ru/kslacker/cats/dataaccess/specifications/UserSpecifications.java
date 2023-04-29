package ru.kslacker.cats.dataaccess.specifications;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.entities.User;

public class UserSpecifications {

	public static Specification<User> withUsername(String username) {
		if (username == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"),
			username));
	}

	public static Specification<User> withEmail(String email) {
		if (email == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email));
	}

	public static Specification<User> withRole(UserRole role) {
		if (role == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role));
	}

	public static Specification<User> withLock(Boolean locked) {
		if (locked == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("locked"),
			locked));
	}

	public static Specification<User> withStatus(Boolean enabled) {
		if (enabled == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"),
			enabled));
	}

	public static Specification<User> withAccountExpirationDate(
		LocalDate accountExpiryDate) {
		if (accountExpiryDate == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(
			root.get("accountExpirationDate"), accountExpiryDate));
	}

	public static Specification<User> withCredentialsExpirationDate(
		LocalDate credentialsExpirationDate) {
		if (credentialsExpirationDate == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(
			root.get("credentialsExpirationDate"), credentialsExpirationDate));
	}
}
