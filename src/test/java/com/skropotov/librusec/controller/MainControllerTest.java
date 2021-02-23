package com.skropotov.librusec.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.skropotov.librusec.controller.tree.GenreTree;
import com.skropotov.librusec.loader.AuthorInfo;
import com.skropotov.librusec.loader.BookInfo;
import com.skropotov.librusec.loader.BookLoader;
import com.skropotov.librusec.model.Book;
import com.skropotov.librusec.model.Genre;
import com.skropotov.librusec.model.repository.AuthorRepository;
import com.skropotov.librusec.model.repository.BookRepository;
import com.skropotov.librusec.model.repository.GenreRepository;
import com.skropotov.librusec.model.repository.SeriaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	AuthorRepository authorRepository;
	
	@MockBean
	SeriaRepository seriaRepository;
	
	@MockBean
	BookRepository bookRepository;
	
	@MockBean
	GenreRepository genreRepository;
	
	@MockBean
	BookLoader bookLoader;
	
	@Test
	public void TestIndex() throws Exception {
		when(authorRepository.count()).thenReturn(5L);
		when(seriaRepository.count()).thenReturn(3L);
		when(bookRepository.count()).thenReturn(20L);

		Genre genre1 = new Genre(null, "genre1");
		genre1.setId(1L);
		Genre genre2 = new Genre(genre1, "genre2");
		genre2.setId(2L);
		Genre genre3 = new Genre(genre2, "genre3");
		genre3.setId(3L);
		when(genreRepository.findByParent(null)).thenReturn(Arrays.asList(genre1));
		
		this.mockMvc.perform(get("/"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("genreCollection", hasSize(3)))
			.andExpect(model().attribute("genreCollection", contains(new GenreTree("1", "genre1", "#"), new GenreTree("2", "genre2", "1"), new GenreTree("3", "genre3", "2"))))
			.andExpect(model().attribute("booksCount", equalTo(20L)))
			.andExpect(model().attribute("seriesCount", equalTo(3L)))
			.andExpect(model().attribute("authorsCount", equalTo(5L)));
	}
	
	@Test
	public void TestLoad() throws Exception {
		MockMultipartFile file = new MockMultipartFile("filenameName", "123.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());
		BookInfo book1 = new BookInfo("name1", "arch1", "filename1", "", null, new Date(), false, "ru", new String[] {}, new AuthorInfo[] {}, 300 );
		BookInfo book2 = new BookInfo("name2", "arch2", "filename2", "", null, new Date(), false, "ru", new String[] {}, new AuthorInfo[] {}, 300 );
		
		when(bookLoader.parseFileDescription("123.txt", file)).thenReturn(Arrays.asList(book1, book2));
		when(bookLoader.saveBook(Mockito.any())).thenReturn(new Book());
		
		mockMvc.perform(multipart("/").file(file).param("filename", "123.txt"))
			.andDo(print())
			.andExpect(status().isFound());
		
		verify(bookLoader, times(2)).saveBook(Mockito.any());
	}
}
