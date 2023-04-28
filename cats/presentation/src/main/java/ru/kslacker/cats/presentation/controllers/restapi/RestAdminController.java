package ru.kslacker.cats.presentation.controllers.restapi;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.presentation.models.users.ExtendedUserModel;
import ru.kslacker.cats.presentation.validation.ValidationGroup;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.models.CatOwnerModel;
import ru.kslacker.cats.services.models.UserUpdateModel;

@RestController
@RequestMapping("api/v3/admin")
@Tag(name = "Admin controller", description = "Controller for system administration")
@SecurityRequirement(name = "BasicAuth")
@PreAuthorize("hasRole(T(ru.kslacker.cats.common.models.UserRole).ADMIN)")
@Validated
public class RestAdminController {

	private final UserService userService;

	@Autowired
	public RestAdminController(UserService userService) {
		this.userService = userService;
	}

	@Validated(ValidationGroup.OnCreate.class)
	@PostMapping(value = "user", produces = "application/json")
	public ResponseEntity<UserDto> createUser(@Valid ExtendedUserModel userModel) {

		return new ResponseEntity<>(userService.create(userModel.credentials(), userModel.role(),
			CatOwnerModel.builder()
				.name(userModel.model().name())
				.dateOfBirth(userModel.model().dateOfBirth())
				.build()),
			HttpStatus.CREATED);
	}

	@PatchMapping(value = "user/{id}/promote", produces = "application/json")
	public ResponseEntity<?> promoteToAdmin(@Positive @PathVariable Long id) {
		userService.promoteToAdmin(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(value = "user/{id}/ban", produces = "application/json")
	public ResponseEntity<?> ban(@Positive @PathVariable Long id) {
		userService.ban(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(value = "user/{id}/unban", produces = "application/json")
	public ResponseEntity<?> unban(@Positive @PathVariable Long id) {
		userService.unban(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(value = "user/{id}/enable", produces = "application/json")
	public ResponseEntity<?> enable(@Positive @PathVariable Long id) {
		userService.enable(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(value = "user/{id}/disable", produces = "application/json")
	public ResponseEntity<?> disable(@Positive @PathVariable Long id) {
		userService.disable(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping(value = "user/{id}", produces = "application/json")
	public ResponseEntity<UserDto> updateUser(@Positive @PathVariable Long id, @Valid @RequestBody ExtendedUserModel userModel) {

		// TODO use extended model
		UserDto user = userService.update(UserUpdateModel.builder()
				.id(id)
				.email(Optional.ofNullable(userModel.credentials().email()))
				.password(Optional.ofNullable(userModel.credentials().password()))
				.enabled(null)
				.locked(null)
				.accountExpirationDate(Optional.empty())
				.credentialsExpirationDate(Optional.empty())
			.build());
		return ResponseEntity.ok(user);
	}

	@DeleteMapping(value = "user/{id}", produces = "application/json")
	public ResponseEntity<?> delete(@Positive @PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

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

		return ResponseEntity.ok(
			userService.getBy(username, email, role, locked, enabled, accountExpirationDate, credentialsExpirationDate, pageable));
	}
}
