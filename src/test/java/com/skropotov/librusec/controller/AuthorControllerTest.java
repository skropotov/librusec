package com.skropotov.librusec.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import com.skropotov.librusec.controller.tree.GenreTree;
import com.skropotov.librusec.model.Author;
import com.skropotov.librusec.model.Genre;
import com.skropotov.librusec.model.repository.AuthorRepository;
import com.skropotov.librusec.model.repository.GenreRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	AuthorRepository authorRepository;
	
	@MockBean
	GenreRepository genreRepository;
	
	@Test
	public void TestAuthorController() throws Exception {
		Genre genre1 = new Genre(null, "genre1");
		genre1.setId(1L);
		Genre genre2 = new Genre(genre1, "genre2");
		genre2.setId(2L);
		Genre genre3 = new Genre(genre2, "genre3");
		genre3.setId(3L);
		when(genreRepository.findByParent(null)).thenReturn(Arrays.asList(genre1));
		
		Author author1 = new Author("Иванов", "Иван", "Иванович");
		Author author2 = new Author("Smith", "John", "");
		when(authorRepository.findByFullNameContainingIgnoreCase("test")).thenReturn(Arrays.asList(author1, author2));

		this.mockMvc.perform(get("/authors?name=test"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(view().name("books/authors"))
			.andExpect(model().attribute("genreCollection", hasSize(3)))
			.andExpect(model().attribute("genreCollection", contains(new GenreTree("1", "genre1", "#"), new GenreTree("2", "genre2", "1"), new GenreTree("3", "genre3", "2"))))
			.andExpect(model().attribute("authors", hasSize(2)))
			.andExpect(model().attribute("authors", contains(author1, author2)));
	}
}
