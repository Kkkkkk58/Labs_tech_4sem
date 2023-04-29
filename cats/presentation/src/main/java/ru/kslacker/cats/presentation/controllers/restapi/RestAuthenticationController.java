package ru.kslacker.cats.presentation.controllers.restapi;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.presentation.models.users.UserModel;
import ru.kslacker.cats.services.api.UserService;
import ru.kslacker.cats.services.dto.UserDto;
import ru.kslacker.cats.services.models.catowners.CatOwnerInformation;

@RestController
@RequestMapping("/api/v3/auth")
@Tag(name = "Authentication", description = "Authentication controller")
public class RestAuthenticationController {

	private final UserService userService;

	@Autowired
	public RestAuthenticationController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Register new user
	 *
	 * @param userModel Information about user
	 * @return Info about registered user
	 */
	@PostMapping(value = "register", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserModel userModel) {

		CatOwnerInformation catOwnerInformation = CatOwnerInformation.builder()
			.name(userModel.catOwnerModel().name())
			.dateOfBirth(userModel.catOwnerModel().dateOfBirth())
			.build();

		UserDto user = userService.create(userModel.credentials(), catOwnerInformation);

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
}
