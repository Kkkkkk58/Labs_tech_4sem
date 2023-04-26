package ru.kslacker.cats.presentation.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import ru.kslacker.cats.common.models.UserRole;
import ru.kslacker.cats.presentation.models.cats.CatModel;
import ru.kslacker.cats.presentation.validation.ValidationGroup;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatUpdateDto;
import ru.kslacker.cats.services.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/v3/cat")
@Tag(name = "Cat", description = "Cat management api")
@SecurityRequirement(name = "BasicAuth")
@Validated
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
	@Validated(ValidationGroup.OnCreate.class)
	@PostMapping(produces = "application/json")
	public ResponseEntity<CatDto> create(@Valid @RequestBody CatModel cat) {
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
	 * Get cat by getId
	 *
	 * @param id Id of the cat
	 * @return Information about the cat with given getId
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
	@Validated(ValidationGroup.OnUpdate.class)
	@PutMapping(value = "{id}", produces = "application/json")
	public ResponseEntity<CatDto> update(@Positive @PathVariable Long id,
		@Valid @RequestBody CatModel updateModel) {

		CatDto cat = service.update(
			CatUpdateDto.builder()
				.id(id)
				.name(updateModel.name())
				.dateOfBirth(updateModel.dateOfBirth())
				.breed(updateModel.breed())
				.furColor(updateModel.furColor())
				.build());

		return ResponseEntity.ok(cat);
	}

	/**
	 * Get cats by filter
	 *
	 * @param name        Cat's name
	 * @param dateOfBirth Cat's date of birth
	 * @param breed       Cat's breed
	 * @param furColor    Cat's fur color
	 * @param ownerId     Cat's owner getId
	 * @param friendsIds  List of cat's friends
	 * @param page        Zero-based page index (0..N)
	 * @param size        The size of the page to be returned
	 * @param sort        Sorting criteria in the format: property,(asc|desc). Default sort order is
	 *                    ascending. Multiple sort criteria are supported.
	 * @return List of cats matching filters
	 */
	@GetMapping(produces = "application/json")
	public ResponseEntity<List<CatDto>> getBy(
		@RequestParam(required = false) String name,
		@RequestParam(value = "date-of-birth", required = false) LocalDate dateOfBirth,
		@RequestParam(required = false) String breed,
		@RequestParam(value = "fur-color", required = false) FurColor furColor,
		@RequestParam(value = "owner-id", required = false) Long ownerId,
		@RequestParam(value = "friends", required = false) List<Long> friendsIds,
		@RequestParam(defaultValue = "0", required = false) Integer page,
		@RequestParam(defaultValue = "20", required = false) Integer size,
		@ParameterObject @RequestParam(required = false) String... sort) {

		Pageable pageable =
			(sort == null) ? PageRequest.of(page, size) : PageRequest.of(page, size, Sort.by(sort));

		UserDetailsImpl userDetails = getUser();
		if (!userDetails.getAuthorities().contains(UserRole.ADMIN)) {
			// TODO throw if other user
			ownerId = userDetails.getOwnerId();
		}

		return ResponseEntity.ok(
			service.getBy(name, dateOfBirth, breed, furColor, ownerId, friendsIds, pageable));
	}

	/**
	 * Add friend to cat
	 *
	 * @param cat1Id Cat's getId to add friend
	 * @param cat2Id New friend's getId
	 * @return None
	 */
	@PatchMapping("{id}/add-friend")
	public ResponseEntity<?> addFriend(
		@Positive @PathVariable(value = "id") Long cat1Id,
		@Positive @RequestParam(value = "friend-id") Long cat2Id) {

		validateUserByCat(cat1Id);

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

		validateUserByCat(cat1Id);

		service.removeFriend(cat1Id, cat2Id);
		return ResponseEntity.noContent().build();
	}

	private UserDetailsImpl getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (UserDetailsImpl) authentication.getPrincipal();
	}

	private void validateUserByCat(Long cat1Id) {
		Long ownerId = getUser().getOwnerId();
		if (!service.get(cat1Id).ownerId().equals(ownerId)) {
			throw new RuntimeException(); // TODO handle
		}
	}
}
