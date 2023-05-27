package ru.kslacker.cats.microservices.restapi.restapi;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserUpdateInformation;
import ru.kslacker.cats.microservices.restapi.security.UserDetailsImpl;
import ru.kslacker.cats.microservices.restapi.services.api.UserService;
import ru.kslacker.cats.microservices.restapi.validation.annotations.Password;

@RestController
@RequestMapping("api/v3/personal")
@Tag(name = "Personal controller", description = "Controller for account management")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "BasicAuth")
@CrossOrigin
@Validated
public class RestPersonalController {

	private final UserService userService;

	@Autowired
	public RestPersonalController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Get personal information
	 *
	 * @return Information about user
	 */
	@GetMapping(produces = "application/json")
	public ResponseEntity<UserDto> getPersonalInfo() {
		return ResponseEntity.ok(userService.get(getPrincipalId()));
	}

	/**
	 * Change user's password
	 *
	 * @param password New password
	 * @param request  Current http request instance to logout
	 * @return None
	 * @throws ServletException General servlet exception
	 */
	@PatchMapping(value = "set-password", produces = "application/json")
	public ResponseEntity<?> changePassword(@RequestParam @Password String password,
		HttpServletRequest request) throws ServletException {

		UserUpdateInformation userUpdateInformation = UserUpdateInformation.builder()
			.id(getPrincipalId())
			.password(password)
			.build();

		userService.update(userUpdateInformation);

		request.logout();
		return ResponseEntity.noContent().build();
	}

	/**
	 * Disable personal account
	 *
	 * @param request Current http request to logout
	 * @return None
	 * @throws ServletException General servlet exception
	 */
	@PatchMapping(value = "disable", produces = "application/json")
	public ResponseEntity<?> disableAccount(HttpServletRequest request) throws ServletException {

		userService.disable(getPrincipalId());
		request.logout();
		return ResponseEntity.noContent().build();
	}

	private Long getPrincipalId() {
		UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
		return principal.getId();
	}
}
