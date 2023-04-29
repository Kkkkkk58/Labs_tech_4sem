package ru.kslacker.cats.test.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.kslacker.cats.common.models.FurColor;
import ru.kslacker.cats.presentation.controllers.restapi.RestCatController;
import ru.kslacker.cats.presentation.controllers.restapi.RestCatOwnerController;
import ru.kslacker.cats.presentation.models.catowners.CatOwnerModel;
import ru.kslacker.cats.presentation.models.cats.CatModel;
import ru.kslacker.cats.services.api.CatOwnerService;
import ru.kslacker.cats.services.api.CatService;
import ru.kslacker.cats.services.dto.CatDto;
import ru.kslacker.cats.services.dto.CatOwnerDto;

@WebMvcTest(controllers = {RestCatController.class, RestCatOwnerController.class})
public class CatsRestApiTest {

	public static final String APPLICATION_JSON = "application/json";
	private static final String API_CAT_OWNER = "/api/v3/cat-owner";
	private static final String API_CAT = "/api/v3/cat";
	private static final String API_ADMIN = "/api/v3/admin";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CatService catService;

	@MockBean
	private CatOwnerService catOwnerService;

	@Test
	public void createValidCatOwner_isCreated() throws Exception {
		CatOwnerModel owner = new CatOwnerModel("Test owner", LocalDate.of(2003, 5, 7));

		mockMvc.perform(post(API_CAT_OWNER)
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(owner))
		).andExpect(status().isCreated());
	}

	@Test
	public void createInvalidCatOwner_isBadRequest() throws Exception {
		CatOwnerModel owner = new CatOwnerModel("", LocalDate.now());

		mockMvc.perform(post(API_CAT_OWNER)
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(owner))
		).andExpect(status().isBadRequest());
	}

	@Test
	public void createValidCat_isCreated() throws Exception {
		CatModel cat = new CatModel("Test cat", LocalDate.of(2003, 5, 7), "TestBreed",
			FurColor.GREY, 1L);

		mockMvc.perform(post(API_CAT)
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(cat))
		).andExpect(status().isCreated());
	}

	@Test
	public void createInvalidCat_isBadRequest() throws Exception {
		CatModel cat = new CatModel("", LocalDate.of(2003, 5, 7), "", FurColor.GREY, 1L);

		mockMvc.perform(post(API_CAT)
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(cat))
		).andExpect(status().isBadRequest());
	}

	@Test
	public void getExistingCatOwner_isOkWithCorrectBody() throws Exception {
		Long catOwnerId = 1L;
		CatOwnerDto catOwnerDto = CatOwnerDto.builder()
			.id(catOwnerId)
			.name("Test owner")
			.dateOfBirth(LocalDate.now())
			.build();
		given(catOwnerService.get(catOwnerId)).willReturn(catOwnerDto);

		MvcResult mvcResult = mockMvc.perform(get(API_CAT_OWNER + "/{id}", catOwnerId)
				.param("id", String.valueOf(catOwnerId)))
			.andExpect(status().isOk())
			.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();
		assertThat(responseBody).isEqualToIgnoringWhitespace(
			objectMapper.writeValueAsString(catOwnerDto));
	}

	@Test
	public void getExistingCat_isOkWithCorrectBody() throws Exception {
		Long catId = 1L;
		CatDto catDto = CatDto.builder()
			.id(catId)
			.name("Test owner")
			.dateOfBirth(LocalDate.now())
			.breed("Test breed")
			.furColor(FurColor.FAWN)
			.ownerId(2L)
			.build();
		given(catService.get(catId)).willReturn(catDto);

		MvcResult mvcResult = mockMvc.perform(get(API_CAT + "/{id}", catId)
				.param("id", String.valueOf(catId)))
			.andExpect(status().isOk())
			.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();
		assertThat(responseBody).isEqualToIgnoringWhitespace(
			objectMapper.writeValueAsString(catDto));
	}

	@Test
	public void updateCatOwnerValidModel_isOk() throws Exception {
		CatOwnerModel owner = new CatOwnerModel(null, LocalDate.of(2003, 5, 7));

		mockMvc.perform(put(API_CAT_OWNER + "/{id}", 1L)
			.param("id", String.valueOf(1L))
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(owner))
		).andExpect(status().isOk());
	}


	@Test
	public void updateCatOwnerInvalidModel_isBadRequest() throws Exception {
		CatOwnerModel owner = new CatOwnerModel("", null);

		mockMvc.perform(put(API_CAT_OWNER + "/{id}", 1L)
			.param("id", String.valueOf(1L))
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(owner))
		).andExpect(status().isBadRequest());
	}

	@Test
	public void updateCatValidModel_isOk() throws Exception {
		CatModel cat = new CatModel("new name", null, null, FurColor.BLACK, null);

		mockMvc.perform(put(API_CAT + "/{id}", 1L)
			.param("id", String.valueOf(1L))
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(cat))
		).andExpect(status().isOk());
	}

	@Test
	public void updateCatInvalidModel_isBadRequest() throws Exception {
		CatModel cat = new CatModel("", null, "", null, -1L);

		mockMvc.perform(put(API_CAT + "/{id}", 1L)
			.param("id", String.valueOf(1L))
			.contentType(APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(cat))
		).andExpect(status().isBadRequest());
	}
}
