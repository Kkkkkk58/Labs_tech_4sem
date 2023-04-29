package ru.kslacker.cats.test.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.User;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.UserRepository;
import ru.kslacker.cats.services.UserServiceImpl;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.mapping.CatOwnerMapping;
import ru.kslacker.cats.services.validation.service.api.ValidationService;

@Component
public class UserServicesTest {

	private UserRepository userRepository;
	private UserService userService;

	@BeforeEach
	public void setup() {
		CatOwnerRepository catOwnerRepository = mock(CatOwnerRepository.class);
		userRepository = mock(UserRepository.class);
		PasswordEncoder encoder = mock(PasswordEncoder.class);
		ValidationService validator = mock(ValidationService.class);

		userService = new UserServiceImpl(validator, encoder, userRepository, catOwnerRepository);
	}

	@Test
	public void createUser_userSaved() {
		String username = "User";
		String password = "Password";
		CatOwner owner = getTestOwner();
		CatOwnerDto ownerDto = CatOwnerMapping.asDto(owner);
		User user = User.builder()
			.withUsername(username)
			.withPassword(password)
			.withOwner(owner)
			.build();
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		UserDto userDto = userService.get(1L);
		Assertions.assertEquals(username, userDto.username());
		Assertions.assertEquals(ownerDto, userDto.owner());
	}

	@Test
	public void disableUser_userDisabled() {
		User user = getTestUser(true, false);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		userService.disable(1L);
		UserDto userDto = userService.get(1L);

		Assertions.assertFalse(userDto.enabled());
	}

	@Test
	public void enableUser_userEnabled() {
		User user = getTestUser(false, true);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		userService.enable(1L);
		UserDto userDto = userService.get(1L);

		Assertions.assertTrue(userDto.enabled());
	}

	@Test
	public void banUser_userBanned() {
		User user = getTestUser(true, false);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		userService.ban(1L);
		UserDto userDto = userService.get(1L);

		Assertions.assertTrue(userDto.locked());
	}

	@Test
	public void unbanUser_userUnbanned() {
		User user = getTestUser(true, true);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		userService.unban(1L);
		UserDto userDto = userService.get(1L);

		Assertions.assertFalse(userDto.locked());
	}

	@Test
	public void promoteUser_userIsAdmin() {
		User user = getTestUser(true, true);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		userService.promoteToAdmin(1L);

		Assertions.assertEquals(UserRole.ADMIN, user.getRole());
	}

	private CatOwner getTestOwner() {
		return new CatOwner("Test owner", LocalDate.now());
	}

	private User getTestUser(boolean enabled, boolean locked) {
		String username = "User";
		String password = "Password";
		CatOwner owner = getTestOwner();

		return User.builder()
			.withUsername(username)
			.withPassword(password)
			.isEnabled(enabled)
			.isLocked(locked)
			.withOwner(owner)
			.build();
	}

}
