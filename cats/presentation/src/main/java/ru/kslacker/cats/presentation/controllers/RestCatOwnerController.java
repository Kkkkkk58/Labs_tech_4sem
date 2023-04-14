package ru.kslacker.cats.presentation.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kslacker.cats.presentation.models.catowners.CatOwnerModel;
import ru.kslacker.cats.presentation.validation.ValidationGroup;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.CatOwnerUpdateDto;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cat-owner")
@Tag(name = "CatOwner", description = "Cat owners' management api")
@Validated
public class RestCatOwnerController {

	private final CatOwnerService service;

	@Autowired
	public RestCatOwnerController(CatOwnerService service) {
		this.service = service;
	}

	/**
	 * Create new cat owner
	 *
	 * @param owner Model of owner to create
	 * @return Information about the created entity
	 */
	@PostMapping(produces = "application/json")
	@Validated(ValidationGroup.OnCreate.class)
	public ResponseEntity<CatOwnerDto> create(@Valid @RequestBody CatOwnerModel owner) {
		return new ResponseEntity<>(service.create(owner.name(), owner.dateOfBirth()),
			HttpStatus.CREATED);
	}

	/**
	 * Get cat owner by id
	 *
	 * @param id Owner's id
	 * @return Information about the cat with given id
	 */
	@GetMapping(value = "{id}", params = {"id"}, produces = "application/json")
	public ResponseEntity<CatOwnerDto> get(@Positive @PathVariable Long id) {
		return ResponseEntity.ok(service.get(id));
	}

	/**
	 * Delete cat owner with given id
	 *
	 * @param id Owner's id
	 * @return None
	 */
	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@Positive @PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Update existing owner with provided data
	 *
	 * @param id    Owner's id
	 * @param owner Structure of update
	 * @return Information about the updated entity
	 */
	@PutMapping(value = "{id}", produces = "application/json")
	@Validated(ValidationGroup.OnUpdate.class)
	public ResponseEntity<CatOwnerDto> update(@Positive @PathVariable Long id,
		@Valid @RequestBody CatOwnerModel owner) {

		CatOwnerDto ownerDto = service.update(
			new CatOwnerUpdateDto(id, owner.name(), owner.dateOfBirth()));

		return ResponseEntity.ok(ownerDto);
	}

	/**
	 * Get owners by filter
	 *
	 * @param name        Owner's name
	 * @param dateOfBirth Owner's date of birth
	 * @param catsIds     List of cats that belong to owner
	 * @param page        Zero-based page index (0..N)
	 * @param size        The size of the page to be returned
	 * @param sort        Sorting criteria in the format: property,(asc|desc). Default sort order is
	 *                    ascending. Multiple sort criteria are supported.
	 * @return List of owners satisfying filter conditions
	 */
	@GetMapping(produces = "application/json")
	public ResponseEntity<List<CatOwnerDto>> getBy(
		@RequestParam(required = false) String name,
		@RequestParam(value = "date-of-birth", required = false) LocalDate dateOfBirth,
		@RequestParam(value = "cats", required = false) List<Long> catsIds,
		@RequestParam(defaultValue = "0", required = false) Integer page,
		@RequestParam(defaultValue = "20", required = false) Integer size,
		@ParameterObject @RequestParam(required = false) Sort sort) {

		Pageable pageable =
			(sort == null) ? PageRequest.of(page, size) : PageRequest.of(page, size, sort);

		return ResponseEntity.ok(service.getBy(name, dateOfBirth, catsIds, pageable));
	}
}
