package com.skropotov.librusec.controller;

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

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import com.skropotov.librusec.controller.tree.GenreTree;
import com.skropotov.librusec.model.Genre;
import com.skropotov.librusec.model.Seria;
import com.skropotov.librusec.model.repository.GenreRepository;
import com.skropotov.librusec.model.repository.SeriaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SeriesControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	SeriaRepository seriaRepository;
	
	@MockBean
	GenreRepository genreRepository;
	
	@Test
	public void TestSeriesController() throws Exception {
		Seria seria1 = new Seria("test1");
		Seria seria2 = new Seria("test2");
		when(seriaRepository.findByNameContainingIgnoreCaseOrderByName("test")).thenReturn(Arrays.asList(seria1, seria2));
		
		Genre genre1 = new Genre(null, "genre1");
		genre1.setId(1L);
		Genre genre2 = new Genre(genre1, "genre2");
		genre2.setId(2L);
		Genre genre3 = new Genre(genre2, "genre3");
		genre3.setId(3L);
		when(genreRepository.findByParent(null)).thenReturn(Arrays.asList(genre1));
		
		this.mockMvc.perform(get("/series?name=test"))
			.andDo(print())
			.andExpect(view().name("books/series"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("series", hasSize(2)))
			.andExpect(model().attribute("series", contains(seria1, seria2)))
			.andExpect(model().attribute("genreCollection", hasSize(3)))
			.andExpect(model().attribute("genreCollection", 
					contains(new GenreTree("1", "genre1", "#"), new GenreTree("2", "genre2", "1"), new GenreTree("3", "genre3", "2"))));
	}
}
