package ru.kslacker.cats.microservices.cats.dataaccess.specifications;


import org.springframework.data.jpa.domain.Specification;
import ru.kslacker.cats.microservices.jpaentities.entities.Cat;
import ru.kslacker.cats.microservices.jpaentities.models.FurColor;
import java.time.LocalDate;

public class CatSpecifications {

	public static Specification<Cat> withName(String name) {
		if (name == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"),
			name));
	}


	public static Specification<Cat> withDateOfBirth(LocalDate dateOfBirth) {
		if (dateOfBirth == null) {
			return null;
		}

		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfBirth"),
			dateOfBirth));
	}

	public static Specification<Cat> withBreed(String breed) {
		if (breed == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("breed"),
			breed));

	}

	public static Specification<Cat> withFurColor(FurColor furColor) {
		if (furColor == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("furColor"),
			furColor));

	}

	public static Specification<Cat> withOwnerId(Long ownerId) {
		if (ownerId == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(
			root.get("owner").get("id"), ownerId));
	}

	public static Specification<Cat> withFriend(Cat friend) {
		if (friend == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.isMember(friend,
			root.get("friends")));
	}
}
