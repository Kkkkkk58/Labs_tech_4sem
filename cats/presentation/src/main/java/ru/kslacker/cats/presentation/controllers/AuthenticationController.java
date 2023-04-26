package ru.kslacker.cats.presentation.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.presentation.models.users.CreateUserModel;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.UserDto;

// TODO Update password

@RestController
@RequestMapping("/")
@Tag(name = "Authentication", description = "Authentication controller")
public class AuthenticationController {

	private final CatOwnerService catOwnerService;
	private final UserService userService;

	@Autowired
	public AuthenticationController(
		CatOwnerService catOwnerService,
		UserService userService) {
		this.catOwnerService = catOwnerService;
		this.userService = userService;
	}

	@PostMapping(value = "register", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody CreateUserModel createUserModel, HttpServletResponse response) {
		CatOwnerDto owner = catOwnerService.create(createUserModel.catOwnerModel().name(), createUserModel.catOwnerModel()
			.dateOfBirth());
		UserDto user = userService.create(createUserModel.username(),
			createUserModel.email().orElse(null), createUserModel.password(), owner.id());

//		response.sendRedirect("/");

		return new ResponseEntity<>(
			user,
			HttpStatus.CREATED);
	}
}

// TODO create user -> then page with owner creation