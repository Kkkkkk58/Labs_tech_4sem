package ru.kslacker.cats.presentation.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.presentation.models.catowners.CreateCatOwnerModel;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;

@RestController
@RequestMapping("/api/cat-owner")
public class RestCatOwnerController {

	private final CatOwnerService service;

	@Autowired
	public RestCatOwnerController(CatOwnerService service) {
		this.service = service;
	}

	@PostMapping(produces = "application/json")
	public ResponseEntity<CatOwnerDto> create(@Valid @RequestBody CreateCatOwnerModel owner) {
		return new ResponseEntity<>(service.create(owner.name(), owner.dateOfBirth()), HttpStatus.CREATED);
	}

	@GetMapping(value = "{id}", params = {"id"}, produces = "application/json")
	public ResponseEntity<CatOwnerDto> get(@Positive @PathVariable Long id) {
		return ResponseEntity.ok(service.get(id));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@Positive @PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<List<CatOwnerDto>> getBy(
		@RequestParam(required = false) String name,
		@RequestParam(value = "date-of-birth", required = false) LocalDate dateOfBirth) {

		Map<String, Object> params = new HashMap<>();
		if (name != null) {
			params.put("name", name);
		}
		if (dateOfBirth != null) {
			params.put("dateOfBirth", dateOfBirth);
		}

		return ResponseEntity.ok(service.getBy(params));
	}
}
