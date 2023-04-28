package ru.kslacker.cats.services;

import static org.springframework.data.jpa.domain.Specification.where;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withAccountExpirationDate;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withCredentialsExpirationDate;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withEmail;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withLock;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withRole;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withStatus;
import static ru.kslacker.cats.dataaccess.specifications.UserSpecifications.withUsername;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.UserAccount;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.UserRepository;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.exceptions.EntityException;
import ru.kslacker.cats.services.exceptions.UserException;
import ru.kslacker.cats.services.mapping.StreamMapping;
import ru.kslacker.cats.services.mapping.UserMapping;
import ru.kslacker.cats.services.models.CatOwnerModel;
import ru.kslacker.cats.services.models.Credentials;
import ru.kslacker.cats.services.models.UserUpdateModel;

@Service
@Transactional(readOnly = true)
@ExtensionMethod({UserMapping.class, StreamMapping.class})
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final CatOwnerRepository catOwnerRepository;
	private final Validator validator;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, CatOwnerRepository catOwnerRepository,
		Validator validator, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.catOwnerRepository = catOwnerRepository;
		this.validator = validator;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public UserDto create(Credentials credentials, UserRole role, CatOwnerModel catOwnerModel) {

		CatOwner owner = new CatOwner(catOwnerModel.name(), catOwnerModel.dateOfBirth());
		owner = catOwnerRepository.save(owner);

		UserAccount userAccount = UserAccount.builder()
			.withUsername(credentials.username())
			.withPassword(passwordEncoder.encode(credentials.password()))
			.withEmail(credentials.email())
			.withRole(role)
			.withOwner(owner)
			.build();

		return userRepository.saveAndFlush(userAccount).asDto();
	}

	@Override
	@Transactional
	public UserDto create(Credentials credentials, CatOwnerModel catOwnerModel) {
		return create(credentials, UserRole.USER, catOwnerModel);
	}

	@Override
	public UserDto get(Long id) {
		return getUserById(id).asDto();
	}

	@Override
	public List<UserDto> getBy(String username, String email, UserRole role, Boolean locked, Boolean enabled,
		LocalDate accountExpirationDate, LocalDate credentialsExpirationDate, Pageable pageable) {

		Specification<UserAccount> specification =
			where(withUsername(username))
				.and(withEmail(email))
				.and(withRole(role))
				.and(withLock(locked))
				.and(withStatus(enabled))
				.and(withAccountExpirationDate(accountExpirationDate))
				.and(withCredentialsExpirationDate(credentialsExpirationDate));

		return userRepository.findAll(specification, pageable).stream().asUserDto().toList();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		UserAccount userAccount = getUserById(id);
		userRepository.delete(userAccount);
	}

	@Override
	@Transactional
	public void disable(Long id) {
		UserAccount user = getUserById(id);
		if (!user.isEnabled()) {
			throw UserException.userAlreadyDisabled(id);
		}

		user.setEnabled(false);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void enable(Long id) {
		UserAccount user = getUserById(id);
		if (user.isEnabled()) {
			throw UserException.userAlreadyEnabled(id);
		}

		user.setEnabled(true);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void ban(Long id) {
		UserAccount user = getUserById(id);
		if (user.isLocked()) {
			throw UserException.userAlreadyLocked(id);
		}

		user.setLocked(true);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public void unban(Long id) {
		UserAccount user = getUserById(id);
		if (!user.isLocked()) {
			throw UserException.userAlreadyUnlocked(id);
		}

		user.setLocked(false);
		userRepository.save(user);
	}

	@Override
	@Transactional
	public UserDto update(UserUpdateModel updateModel) {

		validateUpdateModel(updateModel);

		UserAccount user = getUserById(updateModel.id());

		if (updateModel.email().isPresent()) {
			user.setEmail(updateModel.email().get());
		}
		if (updateModel.password().isPresent()) {
			user.setPassword(passwordEncoder.encode(updateModel.password().get()));
		}
		if (updateModel.enabled() != null) {
			user.setEnabled(updateModel.enabled());
		}
		if (updateModel.locked() != null) {
			user.setLocked(updateModel.locked());
		}
		if (updateModel.accountExpirationDate().isPresent()) {
			user.setAccountExpirationDate(updateModel.accountExpirationDate().get());
		}
		if (updateModel.credentialsExpirationDate().isPresent()) {
			user.setCredentialsExpirationDate(updateModel.credentialsExpirationDate().get());
		}

		return userRepository.save(user).asDto();
	}

	@Override
	@Transactional
	public void promoteToAdmin(Long id) {
		UserAccount user = getUserById(id);
		if (user.getRole().equals(UserRole.ADMIN)) {
			throw UserException.userAlreadyAdmin(id);
		}
		user.setRole(UserRole.ADMIN);

		userRepository.save(user);
	}

	private void validateUpdateModel(UserUpdateModel updateModel) {
		Set<ConstraintViolation<UserUpdateModel>> violations = validator.validate(updateModel);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

	public UserAccount getUserById(Long id) {
		return userRepository
			.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(UserAccount.class, id));
	}
}
