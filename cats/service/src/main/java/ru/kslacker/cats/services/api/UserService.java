package ru.kslacker.cats.services.api;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.models.CatOwnerModel;
import ru.kslacker.cats.services.models.Credentials;
import ru.kslacker.cats.services.models.UserUpdateModel;

public interface UserService {

	UserDto create(Credentials credentials, UserRole role, CatOwnerModel catOwnerModel);
	UserDto create(Credentials credentials, CatOwnerModel catOwnerModel);
	UserDto get(Long id);
	List<UserDto> getBy(String username, String email, UserRole role, Boolean locked, Boolean enabled, LocalDate accountExpirationDate, LocalDate credentialsExpirationDate, Pageable pageable);
	void delete(Long id);
	void disable(Long id);
	void enable(Long id);
	void ban(Long id);
	void unban(Long id);
	UserDto update(UserUpdateModel updateModel);
	void promoteToAdmin(Long id);
}
