package ru.kslacker.cats.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole(T(ru.kslacker.cats.common.models.UserRole).ADMIN)")
public class AdminController {

	// TODO SecurityRequirement
//	@SecurityRequirement(name = "")
	@GetMapping
	public ResponseEntity<String> test() {
		return ResponseEntity.ok("H");
	}

}
