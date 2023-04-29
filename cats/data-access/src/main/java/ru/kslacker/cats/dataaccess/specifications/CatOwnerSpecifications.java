package ru.kslacker.cats.dataaccess.specifications;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import ru.kslacker.cats.dataaccess.entities.Cat;
import ru.kslacker.cats.dataaccess.entities.CatOwner;

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

	public static Specification<CatOwner> withCat(Cat cat) {
		if (cat == null) {
			return null;
		}
		return ((root, query, criteriaBuilder) -> criteriaBuilder.isMember(cat,
			root.get("cats")));
	}
}
