package ru.kslacker.cats.microservices.restapi.restapi;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.microservices.restapi.models.inherited.CatOwnerInformation;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserCreateDto;
import ru.kslacker.cats.microservices.restapi.models.inherited.UserDto;
import ru.kslacker.cats.microservices.restapi.models.users.UserModel;
import ru.kslacker.cats.microservices.restapi.services.api.UserService;

@RestController
@RequestMapping("/api/v3/auth")
@Tag(name = "Authentication", description = "Authentication controller")
@CrossOrigin
@Validated
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

		UserCreateDto createDto = UserCreateDto
			.builder()
			.credentials(userModel.credentials())
			.catOwnerInformation(catOwnerInformation)
			.build();

		UserDto user = userService.create(createDto);

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
}
