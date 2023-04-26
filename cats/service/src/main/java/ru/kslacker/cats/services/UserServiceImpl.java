package ru.kslacker.cats.services;

import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.cats.dataaccess.entities.CatOwner;
import ru.kslacker.cats.dataaccess.entities.UserAccount;
import ru.kslacker.cats.dataaccess.repositories.CatOwnerRepository;
import ru.kslacker.cats.dataaccess.repositories.UserRepository;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.exceptions.EntityException;
import ru.kslacker.cats.services.mapping.UserMapping;

@Service
@Transactional(readOnly = true)
@ExtensionMethod(UserMapping.class)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final CatOwnerRepository catOwnerRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, CatOwnerRepository catOwnerRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.catOwnerRepository = catOwnerRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// TODO maybe with creation of owner??
	// TODO update (password // email)
	// TODO add admin service set expiration dates and enabled / locked
	@Override
	@Transactional
	public UserDto create(String username, String email, String password, Long ownerId) {
		CatOwner owner = getOwnerById(ownerId);
		UserAccount userAccount = UserAccount.builder()
			.withUsername(username)
			.withPassword(passwordEncoder.encode(password))
			.withEmail(email)
			.withOwner(owner)
			.build();

		return userRepository.saveAndFlush(userAccount).asDto();
	}

	@Override
	public UserDto get(Long id) {
		return getUserById(id).asDto();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		UserAccount userAccount = getUserById(id);
		userRepository.delete(userAccount);
	}

	public UserAccount getUserById(Long id) {
		return userRepository
			.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(UserAccount.class, id));
	}

	public CatOwner getOwnerById(Long id) {
		return catOwnerRepository
			.findById(id)
			.orElseThrow(() -> EntityException.entityNotFound(CatOwner.class, id));
	}
}
