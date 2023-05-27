package ru.kslacker.cats.microservices.restapi.restapi;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.microservices.jpaentities.models.UserRole;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserCreateDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserSearchOptions;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserUpdateInformation;
import ru.kslacker.cats.microservices.restapi.models.users.ExtendedUserModel;
import ru.kslacker.cats.microservices.restapi.models.users.UserUpdateModel;
import ru.kslacker.cats.microservices.restapi.services.api.UserService;
import ru.kslacker.cats.microservices.restapi.validation.ValidationGroup;

@RestController
@RequestMapping("api/v3/admin")
@Tag(name = "Admin controller", description = "Controller for system administration")
@SecurityRequirement(name = "BasicAuth")
@PreAuthorize("hasRole(T(ru.kslacker.cats.microservices.jpaentities.models.UserRole).ADMIN)")
@CrossOrigin
@Validated
public class RestAdminController {

	private final UserService userService;

	@Autowired
	public RestAdminController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Create user
	 *
	 * @param userModel Information about user
	 * @return Information about created user
	 */
	@Validated(ValidationGroup.OnCreate.class)
	@PostMapping(value = "user", produces = "application/json")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody ExtendedUserModel userModel) {

		CatOwnerInformation catOwnerInformation = CatOwnerInformation.builder()
			.name(userModel.catOwnerModel().name())
			.dateOfBirth(userModel.catOwnerModel().dateOfBirth())
			.build();

		return new ResponseEntity<>(
			userService.create(new UserCreateDto(userModel.credentials(), userModel.role(), catOwnerInformation)),
			HttpStatus.CREATED);
	}

	/**
	 * Promote user with given id to admin
	 *
	 * @param id User's id
	 * @return None
	 */
	@PatchMapping(value = "user/{id}/promote", produces = "application/json")
	public ResponseEntity<?> promoteToAdmin(@Positive @PathVariable Long id) {

		userService.promoteToAdmin(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Ban user with given id
	 *
	 * @param id User's id
	 * @return None
	 */
	@PatchMapping(value = "user/{id}/ban", produces = "application/json")
	public ResponseEntity<?> ban(@Positive @PathVariable Long id) {
		userService.ban(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Unban user with given id
	 *
	 * @param id User's id
	 * @return None
	 */
	@PatchMapping(value = "user/{id}/unban", produces = "application/json")
	public ResponseEntity<?> unban(@Positive @PathVariable Long id) {
		userService.unban(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Enable user's account with given id
	 *
	 * @param id User's id
	 * @return None
	 */
	@PatchMapping(value = "user/{id}/enable", produces = "application/json")
	public ResponseEntity<?> enable(@Positive @PathVariable Long id) {
		userService.enable(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Disable user's account with given id
	 *
	 * @param id User's id
	 * @return None
	 */
	@PatchMapping(value = "user/{id}/disable", produces = "application/json")
	public ResponseEntity<?> disable(@Positive @PathVariable Long id) {
		userService.disable(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Update user with given id
	 *
	 * @param id        User's id
	 * @param userModel Structure of update
	 * @return Updated user
	 */
	@PutMapping(value = "user/{id}", produces = "application/json")
	public ResponseEntity<UserDto> updateUser(
		@Positive @PathVariable Long id,
		@Valid @RequestBody UserUpdateModel userModel) {

		UserUpdateInformation updateInformation = UserUpdateInformation.builder()
			.id(id)
			.email(userModel.email())
			.password(userModel.password())
			.enabled(userModel.enabled())
			.locked(userModel.locked())
			.accountExpirationDate(userModel.accountExpirationDate())
			.credentialsExpirationDate(userModel.credentialsExpirationDate())
			.build();

		UserDto user = userService.update(updateInformation);
		return ResponseEntity.ok(user);
	}

	/**
	 * Delete user with given id
	 *
	 * @param id User's id
	 * @return None
	 */
	@DeleteMapping(value = "user/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@Positive @PathVariable Long id) {

		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Get users by filter
	 *
	 * @param username                  User's username
	 * @param email                     User's email
	 * @param role                      User's role
	 * @param locked                    User's locked status
	 * @param enabled                   User's enabled status
	 * @param accountExpirationDate     User's account expiration date
	 * @param credentialsExpirationDate User's credentials expiration date
	 * @param page                      Zero-based page index (0..N)
	 * @param size                      The size of the page to be returned
	 * @param sort                      Sorting criteria in the format: property,(asc|desc). Default
	 *                                  sort order is ascending. Multiple sort criteria are
	 *                                  supported.
	 * @return List of users satisfying filter conditions
	 */
	@GetMapping(value = "user", produces = "application/json")
	public ResponseEntity<List<UserDto>> getBy(
		@RequestParam(required = false) String username,
		@RequestParam(required = false) String email,
		@RequestParam(required = false) UserRole role,
		@RequestParam(required = false) Boolean locked,
		@RequestParam(required = false) Boolean enabled,
		@RequestParam(value = "account-expiration-date", required = false) LocalDate accountExpirationDate,
		@RequestParam(value = "credentials-expiration-date", required = false) LocalDate credentialsExpirationDate,
		@RequestParam(defaultValue = "0", required = false) Integer page,
		@RequestParam(defaultValue = "20", required = false) Integer size,
		@ParameterObject @RequestParam(required = false) String... sort) {

		Pageable pageable =
			(sort == null) ? PageRequest.of(page, size) : PageRequest.of(page, size, Sort.by(sort));

		UserSearchOptions searchOptions = UserSearchOptions.builder()
			.username(username)
			.email(email)
			.role(role)
			.enabled(enabled)
			.locked(locked)
			.accountExpirationDate(accountExpirationDate)
			.credentialsExpirationDate(credentialsExpirationDate)
			.pageable(pageable)
			.build();

		return ResponseEntity.ok(userService.getBy(searchOptions));
	}
}
