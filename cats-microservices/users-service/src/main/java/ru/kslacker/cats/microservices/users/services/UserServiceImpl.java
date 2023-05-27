package ru.kslacker.cats.microservices.users.services;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.kslacker.cats.microservices.users.dataaccess.specifications.UserSpecifications.withAccountExpirationDate;
import static ru.kslacker.cats.microservices.users.dataaccess.specifications.UserSpecifications.withCredentialsExpirationDate;
import static ru.kslacker.cats.microservices.users.dataaccess.specifications.UserSpecifications.withEmail;
import static ru.kslacker.cats.microservices.users.dataaccess.specifications.UserSpecifications.withLock;
import static ru.kslacker.cats.microservices.users.dataaccess.specifications.UserSpecifications.withRole;
import static ru.kslacker.cats.microservices.users.dataaccess.specifications.UserSpecifications.withStatus;
import static ru.kslacker.cats.microservices.users.dataaccess.specifications.UserSpecifications.withUsername;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.NonNull;
import lombok.experimental.ExtensionMethod;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.microservices.common.amqp.api.AmqpRelyingService;
import ru.kslacker.cats.microservices.jpaentities.entities.User;
import ru.kslacker.cats.microservices.jpaentities.exceptions.EntityException;
import ru.kslacker.cats.microservices.jpaentities.models.UserRole;
import ru.kslacker.cats.microservices.users.dataaccess.repositories.api.UserRepository;
import ru.kslacker.cats.microservices.users.dto.CatOwnerDto;
import ru.kslacker.cats.microservices.users.dto.UserCreateDto;
import ru.kslacker.cats.microservices.users.dto.UserDetails;
import ru.kslacker.cats.microservices.users.dto.UserDto;
import ru.kslacker.cats.microservices.users.dto.UserSearchOptions;
import ru.kslacker.cats.microservices.users.dto.UserUpdateInformation;
import ru.kslacker.cats.microservices.users.exceptions.UserException;
import ru.kslacker.cats.microservices.users.mapping.UserMapping;
import ru.kslacker.cats.microservices.users.services.api.UserService;
import ru.kslacker.cats.microservices.users.validation.service.api.ValidationService;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({UserMapping.class})
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ValidationService validator;
	private final PasswordEncoder passwordEncoder;
	private final AmqpRelyingService amqpService;

	@Autowired
	public UserServiceImpl(
		@NonNull ValidationService validator,
		@NonNull PasswordEncoder passwordEncoder,
		@NonNull UserRepository userRepository,
		@NonNull AmqpRelyingService amqpService) {

		this.validator = validator;
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.amqpService = amqpService;
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{createUserQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public UserDto create(UserCreateDto createDto) {

		validator.validate(createDto.credentials());

		Long ownerId = null;
		if (createDto.catOwnerInformation() != null) {

			validator.validate(createDto.catOwnerInformation());

			ownerId = amqpService.handleRequest("owner.create", createDto.catOwnerInformation(), CatOwnerDto.class).id();
		}

		UserRole role = createDto.userRole();
		if (role == null) {
			role = UserRole.USER;
		}

		User user = User.builder()
			.withUsername(createDto.credentials().username())
			.withPassword(passwordEncoder.encode(createDto.credentials().password()))
			.withEmail(createDto.credentials().email().orElse(null))
			.withRole(role)
			.withOwnerId(ownerId)
			.build();

		return userRepository.saveAndFlush(user).asDto();
	}

	@Override
	@RabbitListener(queues = "#{getUserQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public UserDto get(@NonNull Long id) {
		return getUserById(id).asDto();
	}

	@Override
	@RabbitListener(queues = "#{getUserByParamsQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public List<UserDto> getBy(@NonNull UserSearchOptions searchOptions) {

		Specification<User> specification =
			where(withUsername(searchOptions.username()))
				.and(withEmail(searchOptions.email()))
				.and(withRole(searchOptions.role()))
				.and(withLock(searchOptions.locked()))
				.and(withStatus(searchOptions.enabled()))
				.and(withAccountExpirationDate(searchOptions.accountExpirationDate()))
				.and(withCredentialsExpirationDate(searchOptions.credentialsExpirationDate()));

		return userRepository.findAll(specification, searchOptions.pageable()).stream().asUserDto().toList();
	}

	@Override
	@RabbitListener(queues = "#{getUserByUsernameQueue}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public UserDetails loadByUsername(String username) {

		User user = userRepository.findByUsername(username).orElseThrow(() -> EntityException.entityNotFound(User.class, username));

		return UserDetails
			.builder()
			.authorities(Set.of(user.getRole()))
			.password(user.getPassword())
			.username(user.getUsername())
			.accountExpirationDate(user.getAccountExpirationDate())
			.isLocked(user.isLocked())
			.credentialsExpirationDate(user.getCredentialsExpirationDate())
			.isEnabled(user.isEnabled())
			.email(user.getEmail())
			.id(user.getId())
			.ownerId(user.getOwnerId())
			.build();

	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{deleteUserQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public boolean delete(Long id) {

		User user = getUserById(id);

		if (user.getOwnerId() != null) {

			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.submit(() -> deleteOwner(user));
		}

		userRepository.delete(user);
		return true;
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{disableUserQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public UserDto disable(Long id) {
		User user = getUserById(id);
		if (!user.isEnabled()) {
			throw UserException.userAlreadyDisabled(id);
		}

		user.setEnabled(false);
		return userRepository.save(user).asDto();
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{enableUserQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public UserDto enable(Long id) {

		User user = getUserById(id);
		if (user.isEnabled()) {
			throw UserException.userAlreadyEnabled(id);
		}

		user.setEnabled(true);
		return userRepository.save(user).asDto();
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{banUserQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public UserDto ban(Long id) {

		User user = getUserById(id);
		if (user.isLocked()) {
			throw UserException.userAlreadyLocked(id);
		}

		user.setLocked(true);
		return userRepository.save(user).asDto();
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{unbanUserQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public UserDto unban(Long id) {

		User user = getUserById(id);
		if (!user.isLocked()) {
			throw UserException.userAlreadyUnlocked(id);
		}

		user.setLocked(false);
		return userRepository.save(user).asDto();
	}

	@Override
	@Transactional
	@RabbitListener(queues = "#{updateUserQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
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
	@RabbitListener(queues = "#{promoteUserToAdminQueue.name}", group = "#{amqpGroupName}", returnExceptions = "true", errorHandler = "#{rabbitErrorHandler}")
	public UserDto promoteToAdmin(Long id) {
		User user = getUserById(id);
		if (user.getRole().equals(UserRole.ADMIN)) {
			throw UserException.userAlreadyAdmin(id);
		}
		user.setRole(UserRole.ADMIN);

		return userRepository.save(user).asDto();
	}

	private User getUserById(Long id) {
		return userRepository
			.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(User.class, id));
	}

	private void deleteOwner(User user) {
		try {
			amqpService.handleRequest("owner.delete", user.getOwnerId(), Boolean.class);
		} catch (EntityException ignored) {

		}
	}
}
