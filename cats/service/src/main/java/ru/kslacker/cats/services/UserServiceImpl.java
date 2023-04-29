package ru.kslacker.cats.services;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withAccountExpirationDate;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withCredentialsExpirationDate;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withEmail;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withLock;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withRole;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withStatus;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withUsername;

import java.util.List;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.User;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.UserRepository;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.exceptions.EntityException;
import ru.kslacker.cats.services.exceptions.UserException;
import ru.kslacker.cats.services.mapping.StreamMapping;
import ru.kslacker.cats.services.mapping.UserMapping;
import ru.kslacker.cats.services.models.catowners.CatOwnerInformation;
import ru.kslacker.cats.services.models.users.Credentials;
import ru.kslacker.cats.services.models.users.UserSearchOptions;
import ru.kslacker.cats.services.models.users.UserUpdateInformation;
import ru.kslacker.cats.services.validation.service.api.ValidationService;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({UserMapping.class, StreamMapping.class})
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final CatOwnerRepository catOwnerRepository;
	private final ValidationService validator;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(
		@NonNull ValidationService validator,
		@NonNull PasswordEncoder passwordEncoder,
		@NonNull UserRepository userRepository,
		@NonNull CatOwnerRepository catOwnerRepository) {

		this.validator = validator;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.catOwnerRepository = catOwnerRepository;
	}

	@Override
	@Transactional
	public UserDto create(
		@NonNull Credentials credentials,
		@NonNull UserRole role,
		CatOwnerInformation catOwnerInformation) {

		validator.validate(credentials);

		CatOwner owner = null;
		if (catOwnerInformation != null) {

			validator.validate(catOwnerInformation);
			owner = catOwnerRepository.save(
				new CatOwner(catOwnerInformation.name(), catOwnerInformation.dateOfBirth()));
		}

		User user = User.builder()
			.withUsername(credentials.username())
			.withPassword(passwordEncoder.encode(credentials.password()))
			.withEmail(credentials.email().orElse(null))
			.withRole(role)
			.withOwner(owner)
			.build();

		return userRepository.saveAndFlush(user).asDto();
	}

	@Override
	@Transactional
	public UserDto create(
		@NonNull Credentials credentials,
		@NonNull CatOwnerInformation catOwnerInformation) {

		return create(credentials, UserRole.USER, catOwnerInformation);
	}

	@Override
	public UserDto get(@NonNull Long id) {
		return getUserById(id).asDto();
	}

	@Override
	public List<UserDto> getBy(@NonNull UserSearchOptions searchOptions) {

		Specification<User> specification =
			where(withUsername(searchOptions.username()))
				.and(withEmail(searchOptions.email()))
				.and(withRole(searchOptions.role()))
				.and(withLock(searchOptions.locked()))
				.and(withStatus(searchOptions.enabled()))
				.and(withAccountExpirationDate(searchOptions.accountExpirationDate()))
				.and(withCredentialsExpirationDate(searchOptions.credentialsExpirationDate()));

		return userRepository.findAll(specification, searchOptions.pageable()).stream().asUserDto()
			.toList();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		User user = getUserById(id);
		userRepository.delete(user);
	}

	@Override
	@Transactional
	public void disable(Long id) {
		User user = getUserById(id);
		if (!user.isEnabled()) {
			throw UserException.userAlreadyDisabled(id);
		}

		user.setEnabled(false);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void enable(Long id) {

		User user = getUserById(id);
		if (user.isEnabled()) {
			throw UserException.userAlreadyEnabled(id);
		}

		user.setEnabled(true);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void ban(Long id) {

		User user = getUserById(id);
		if (user.isLocked()) {
			throw UserException.userAlreadyLocked(id);
		}

		user.setLocked(true);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void unban(Long id) {

		User user = getUserById(id);
		if (!user.isLocked()) {
			throw UserException.userAlreadyUnlocked(id);
		}

		user.setLocked(false);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public UserDto update(UserUpdateInformation userUpdateInformation) {

		validator.validate(userUpdateInformation);

		User user = getUserById(userUpdateInformation.id());

		if (userUpdateInformation.email() != null) {
			user.setEmail(userUpdateInformation.email());
		}
		if (userUpdateInformation.password() != null) {
			user.setPassword(passwordEncoder.encode(userUpdateInformation.password()));
		}
		if (userUpdateInformation.enabled() != null) {
			user.setEnabled(userUpdateInformation.enabled());
		}
		if (userUpdateInformation.locked() != null) {
			user.setLocked(userUpdateInformation.locked());
		}
		if (userUpdateInformation.accountExpirationDate() != null) {
			user.setAccountExpirationDate(userUpdateInformation.accountExpirationDate());
		}
		if (userUpdateInformation.credentialsExpirationDate() != null) {
			user.setCredentialsExpirationDate(
				userUpdateInformation.credentialsExpirationDate());
		}

		return userRepository.save(user).asDto();
	}

	@Override
	@Transactional
	public void promoteToAdmin(Long id) {
		User user = getUserById(id);
		if (user.getRole().equals(UserRole.ADMIN)) {
			throw UserException.userAlreadyAdmin(id);
		}
		user.setRole(UserRole.ADMIN);

		userRepository.save(user);
	}

	public User getUserById(Long id) {
		return userRepository
			.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(User.class, id));
	}
}
