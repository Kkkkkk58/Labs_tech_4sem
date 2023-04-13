package ru.kslacker.cats.presentation.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.presentation.models.catowners.CreateCatOwnerModel;
import ru.kslacker.cats.presentation.models.catowners.UpdateCatOwnerModel;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.dto.CatOwnerDto;
import ru.kslacker.cats.services.dto.CatOwnerUpdateDto;

@RestController
@RequestMapping("/api/cat-owner")
@Tag(name = "CatOwner", description = "Cat owners' management api")
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
	public ResponseEntity<CatOwnerDto> create(@Valid @RequestBody CreateCatOwnerModel owner) {
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
	 * @param id          Owner's id
	 * @param updateModel Structure of update
	 * @return Information about the updated entity
	 */
	@PutMapping(value = "{id}", produces = "application/json")
	public ResponseEntity<CatOwnerDto> update(@Positive @PathVariable Long id, @RequestBody
	UpdateCatOwnerModel updateModel) {

		CatOwnerDto ownerDto = service.update(
			new CatOwnerUpdateDto(id, updateModel.name(), updateModel.dateOfBirth(),
				updateModel.cats()));

		return ResponseEntity.ok(ownerDto);
	}

	/**
	 * Get owners by filter
	 *
	 * @param name        Owner's name
	 * @param dateOfBirth Owner's date of birth
	 * @return List of owners satisfying filter conditions
	 */
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
