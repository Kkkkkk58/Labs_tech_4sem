package ru.kslacker.cats.microservices.catowners.dataaccess.specifications;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import ru.kslacker.cats.microservices.jpaentities.entities.CatOwner;

public class CatOwnerSpecifications {

	public static Specification<CatOwner> withName(String name) {
		if (name == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"),
			name));
	}

	public static Specification<CatOwner> withDateOfBirth(LocalDate dateOfBirth) {
		if (dateOfBirth == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfBirth"),
			dateOfBirth));
	}

	public static Specification<CatOwner> withCat(Long cat) {
		if (cat == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.isMember(cat,
			root.get("cats").get("id")));
	}
}
