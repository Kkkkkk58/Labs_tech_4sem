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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.presentation.models.cats.CreateCatModel;
import ru.kslacker.cats.presentation.models.cats.UpdateCatModel;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;

@Validated
@RestController
@RequestMapping("/api/cat")
public class RestCatController {

	private final CatService service;

	@Autowired
	public RestCatController(CatService service) {
		this.service = service;
	}


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

	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@Positive @PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "{id}", produces = "application/json")
	public ResponseEntity<CatDto> get(@Positive @PathVariable Long id) {
		return ResponseEntity.ok(service.get(id));
	}

	@PutMapping(value = "{id}", produces = "application/json")
	public ResponseEntity<CatDto> update(@Positive @PathVariable Long id, @RequestBody
		UpdateCatModel updateModel) {

		CatDto cat = service.update(new CatDto(id, updateModel.name(), updateModel.dateOfBirth(), updateModel.breed(), updateModel.furColor(), updateModel.ownerId(), updateModel.friends()));
		return ResponseEntity.ok(cat);
	}

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

	// TODO add GET to take all or top

	@PatchMapping("make-friends")
	public ResponseEntity<?> makeFriends(
		@Positive @RequestParam(value = "cat1") Long cat1Id,
		@Positive @RequestParam(value = "cat2") Long cat2Id) {

		service.makeFriends(cat1Id, cat2Id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("{id}/remove-friend")
	public ResponseEntity<?> removeFriend(
		@Positive @PathVariable(value = "id") Long cat1Id,
		@Positive @RequestParam Long cat2Id) {

		service.removeFriend(cat1Id, cat2Id);
		return ResponseEntity.noContent().build();
	}
}
