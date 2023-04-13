package ru.kslacker.cats.services.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kslacker.cats.dataaccess.repositories.api.CatRepository;
import ru.kslacker.cats.services.dto.CatDto;

public class UpdateCatFriendsListValidator implements ConstraintValidator<ValidUpdateCatFriends, CatDto> {

	@Autowired private CatRepository catService;

	@Override
	public boolean isValid(CatDto value, ConstraintValidatorContext context) {
		return value.friends() == null || (
				!value.friends().contains(value.id()) &&
					value.friends().size() == value.friends().stream().distinct().count() &&
					value.friends().stream().allMatch(id -> catService.existsById(id))
			);
	}
}
