package ru.kslacker.cats.presentation.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.presentation.models.cats.CreateCatModel;
import ru.kslacker.cats.presentation.models.cats.UpdateCatModel;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatUpdateDto;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/cat")
@Tag(name = "Cat", description = "Cat management api")
public class RestCatController {

	private final CatService service;

	@Autowired
	public RestCatController(CatService service) {
		this.service = service;
	}


	/**
	 * Create new cat
	 *
	 * @param cat Model of cat to create
	 * @return Information about the created entity
	 */
	@PostMapping(produces = "application/json")
	public ResponseEntity<CatDto> create(@Valid @RequestBody CreateCatModel cat) {
		return new ResponseEntity<>(
			service.create(
				cat.name(),
				cat.dateOfBirth(),
				cat.breed(),
				cat.furColor(),
				cat.ownerId()),
			HttpStatus.CREATED
		);
	}

	/**
	 * Delete cat by id
	 *
	 * @param id Id of the cat to delete
	 * @return None
	 */
	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@Positive @PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Get cat by id
	 *
	 * @param id Id of the cat
	 * @return Information about the cat with given id
	 */
	@GetMapping(value = "{id}", produces = "application/json")
	public ResponseEntity<CatDto> get(@Positive @PathVariable Long id) {
		return ResponseEntity.ok(service.get(id));
	}

	/**
	 * Update existing cat with provided data
	 *
	 * @param id          Id of the cat to update
	 * @param updateModel Structure of update
	 * @return Information about updated entity
	 */
	@PutMapping(value = "{id}", produces = "application/json")
	public ResponseEntity<CatDto> update(@Positive @PathVariable Long id, @RequestBody
	UpdateCatModel updateModel) {

		CatDto cat = service.update(
			new CatUpdateDto(id, updateModel.name(), updateModel.dateOfBirth(), updateModel.breed(),
				updateModel.furColor(), updateModel.ownerId(), updateModel.friends()));
		return ResponseEntity.ok(cat);
	}

	/**
	 * Get cats by filter
	 *
	 * @param name        Cat's name
	 * @param dateOfBirth Cat's date of birth
	 * @param breed       Cat's breed
	 * @param furColor    Cat's fur color
	 * @param ownerId     Cat's owner id
	 * @return List of cats matching filters
	 */
	@GetMapping(produces = "application/json")
	public ResponseEntity<List<CatDto>> getBy(
		@RequestParam(required = false) String name,
		@RequestParam(value = "date-of-birth", required = false) LocalDate dateOfBirth,
		@RequestParam(required = false) String breed,
		@RequestParam(value = "fur-color", required = false) FurColor furColor,
		@RequestParam(value = "owner-id", required = false) Long ownerId) {

		Map<String, Object> params = new HashMap<>();
		if (name != null) {
			params.put("name", name);
		}
		if (dateOfBirth != null) {
			params.put("dateOfBirth", dateOfBirth);
		}
		if (breed != null) {
			params.put("breed", breed);
		}
		if (furColor != null) {
			params.put("furColor", furColor);
		}
		if (ownerId != null) {
			params.put("owner_id", ownerId);
		}

		return ResponseEntity.ok(service.getBy(params));
	}

	// TODO add GET to take all or top -> paging and sorting + filters ????

	/**
	 * Add friend to cat
	 *
	 * @param cat1Id Cat's id to add friend
	 * @param cat2Id New friend's id
	 * @return None
	 */
	@PatchMapping("{id}/add-friend")
	public ResponseEntity<?> addFriend(
		@Positive @PathVariable(value = "id") Long cat1Id,
		@Positive @RequestParam(value = "friend-id") Long cat2Id) {

		service.makeFriends(cat1Id, cat2Id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Remove cat's friend
	 *
	 * @param cat1Id Id of the cat to remove friend from
	 * @param cat2Id Id of the friend
	 * @return None
	 */
	@PatchMapping("{id}/remove-friend")
	public ResponseEntity<?> removeFriend(
		@Positive @PathVariable(value = "id") Long cat1Id,
		@Positive @RequestParam(value = "friend-id") Long cat2Id) {

		service.removeFriend(cat1Id, cat2Id);
		return ResponseEntity.noContent().build();
	}
}
