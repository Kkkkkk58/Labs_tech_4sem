package ru.kslacker.cats.presentation.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.presentation.models.users.CreateUserModel;
import ru.kslacker.cats.presentation.models.users.LoginModel;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.security.UserDetailsImpl;

import static org.springframework.http.ResponseEntity.ok;

// TODO Update password

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication controller")
public class AuthenticationController {

	private final CatOwnerService catOwnerService;
	private final UserService userService;
	private final AuthenticationManager authenticationManager;

	@Autowired
	public AuthenticationController(
		CatOwnerService catOwnerService,
		UserService userService,
		AuthenticationManager authenticationManager) {
		this.catOwnerService = catOwnerService;
		this.userService = userService;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping(value = "register", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody CreateUserModel createUserModel) {
		CatOwnerDto owner = catOwnerService.create(createUserModel.catOwnerModel().name(), createUserModel.catOwnerModel()
			.dateOfBirth());

		return new ResponseEntity<>(
			userService.create(createUserModel.username(), createUserModel.email().orElse(null), createUserModel.password(), owner.id()),
			HttpStatus.CREATED);
	}

	@PostMapping(value = "login", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserDto> login(@Valid @RequestBody LoginModel loginModel, HttpServletRequest request) {
		return doLogin(loginModel.username(), loginModel.password(), request);
	}

	@GetMapping("test")
	public ResponseEntity<UserDetailsImpl> test() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return ResponseEntity.ok((UserDetailsImpl) authentication.getPrincipal());
	}

	private ResponseEntity<UserDto> doLogin(String username, String password, HttpServletRequest request) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
			username, password);
		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		HttpSession session = request.getSession(true);
		session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return ok(new UserDto(userDetails.getId(), userDetails.getUsername()));
	}
}

// TODO create user -> then page with owner creation