package ru.kslacker.cats.presentation.controllers.restapi;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.models.UserUpdateModel;
import ru.kslacker.cats.services.security.UserDetailsImpl;
import ru.kslacker.cats.services.validation.annotations.Password;

@RestController
@RequestMapping("api/v3/personal")
@Tag(name = "Personal controller", description = "Controller for account management")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "BasicAuth")
public class RestPersonalController {

	private final UserService userService;

	@Autowired
	public RestPersonalController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<UserDto> getPersonalInfo() {
		return ResponseEntity.ok(userService.get(getPrincipalId()));
	}

	@PatchMapping(value = "set-password", produces = "application/json")
	public ResponseEntity<?> changePassword(@RequestParam @Password String password, HttpServletRequest request)
		throws ServletException {

		userService.update(UserUpdateModel.builder()
			.id(getPrincipalId())
			.password(Optional.of(password))
			.build());

		request.logout();
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(value = "disable", produces = "application/json")
	public ResponseEntity<?> disableAccount(HttpServletRequest request) throws ServletException {

		userService.disable(getPrincipalId());
		request.logout();
		return ResponseEntity.noContent().build();
	}

	public Long getPrincipalId() {
		UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal.getId();
	}
}
