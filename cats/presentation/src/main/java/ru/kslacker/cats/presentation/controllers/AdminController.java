package ru.kslacker.cats.presentation.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin controller", description = "Controller for system administration")
@SecurityRequirement(name = "BasicAuth")
@PreAuthorize("hasRole(T(ru.kslacker.cats.common.models.UserRole).ADMIN)")
public class AdminController {

	// TODO SecurityRequirement
//	@SecurityRequirement(name = "")
	@GetMapping
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("H");
	}

}
