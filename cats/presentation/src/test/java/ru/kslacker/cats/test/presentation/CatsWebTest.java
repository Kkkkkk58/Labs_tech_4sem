package ru.kslacker.cats.test.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.kslacker.cats.presentation.controllers.CatController;
import ru.kslacker.cats.presentation.controllers.CatOwnerController;

@WebMvcTest(controllers = {CatController.class, CatOwnerController.class})
public class CatsWebTest {

	@Autowired private MockMvc mockMvc;

}
