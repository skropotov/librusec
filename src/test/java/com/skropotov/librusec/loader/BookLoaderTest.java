package com.skropotov.librusec.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.skropotov.librusec.model.Author;
import com.skropotov.librusec.model.Book;
import com.skropotov.librusec.model.Seria;
import com.skropotov.librusec.model.repository.AuthorRepository;
import com.skropotov.librusec.model.repository.BookRepository;
import com.skropotov.librusec.model.repository.GenreRepository;
import com.skropotov.librusec.model.repository.SeriaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookLoaderTest {
	@MockBean
	AuthorRepository authorRepository;
	
	@MockBean
	SeriaRepository seriaRepository;
	
	@MockBean
	BookRepository bookRepository;
	
	@MockBean
	GenreRepository genreRepository;
	
	@Autowired
	BookLoader bookLoader;
	
	@Test
	public void TestLoadBooks() {
		String bookDescription = "Пилипюк,Анджей,:sf:Carska ManierkaŚwiaty Pilipiuka670130011586897013000fb22020-09-03pl";
		MockMultipartFile file = new MockMultipartFile("filenameName", "123.txt", MediaType.TEXT_PLAIN_VALUE, bookDescription.getBytes());

		List<BookInfo> books = bookLoader.parseFileDescription("fb2-701300-703385.inp", file);
		
		assertEquals(1, books.size());
		assertEquals("Carska Manierka", books.get(0).getName());
		assertEquals("Thu Sep 03 00:00:00 MSK 2020", books.get(0).getLoadedDate().toString());
		assertEquals(1158689, books.get(0).getBookSize());
		assertEquals("pl", books.get(0).getLanguage());
		assertEquals("701300.fb2", books.get(0).getFilename());
		assertEquals("Światy Pilipiuka", books.get(0).getSeria());
		assertEquals(1, books.get(0).getGenres().length);
		assertEquals("sf", books.get(0).getGenres()[0]);
		assertEquals(1, books.get(0).getAuthors().length);
		assertEquals("Пилипюк Анджей", books.get(0).getAuthors()[0].getFullName());
	}

	@Test
	public void TestLoadNonExistBook() {
		String bookDescription = "Пилипюк,Анджей,:sf:Carska ManierkaŚwiaty Pilipiuka670130011586897013000fb22020-09-03pl";
		MockMultipartFile file = new MockMultipartFile("filenameName", "123.txt", MediaType.TEXT_PLAIN_VALUE, bookDescription.getBytes());

		List<BookInfo> books = bookLoader.parseFileDescription("123.inp", file);
		assertEquals(0, books.size());
	}
	
	@Test
	public void TestLoadBookWithoutLanguage() {
		String bookDescription = "Пилипюк,Анджей,:sf:Carska ManierkaŚwiaty Pilipiuka670130011586897013000fb22020-09-03";
		MockMultipartFile file = new MockMultipartFile("filenameName", "123.txt", MediaType.TEXT_PLAIN_VALUE, bookDescription.getBytes());

		List<BookInfo> books = bookLoader.parseFileDescription("fb2-701300-703385.inp", file);

		assertEquals("Carska Manierka", books.get(0).getName());
		assertEquals("Thu Sep 03 00:00:00 MSK 2020", books.get(0).getLoadedDate().toString());
		assertEquals(1158689, books.get(0).getBookSize());
		assertEquals("", books.get(0).getLanguage());
		assertEquals("701300.fb2", books.get(0).getFilename());
		assertEquals("Światy Pilipiuka", books.get(0).getSeria());
		assertEquals(1, books.get(0).getGenres().length);
		assertEquals("sf", books.get(0).getGenres()[0]);
		assertEquals(1, books.get(0).getAuthors().length);
		assertEquals("Пилипюк Анджей", books.get(0).getAuthors()[0].getFullName());
	}
	
	@Test
	public void TestSaveSeriaWhenNonFound() throws ParseException {
		when(seriaRepository.findByName(Mockito.anyString())).thenReturn(new ArrayList<Seria>());
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		BookInfo info = new BookInfo("test name", "archive.zip", "111.fb2", "seria", 1L, format.parse("2020-01-01"), false, "ru", new String[] {}, new AuthorInfo[] {}, 555);
		bookLoader.saveBook(info);
		
		verify(seriaRepository, times(1)).save(Mockito.any());
		verify(seriaRepository).save(new Seria("seria"));
	}

	@Test
	public void TestNonSaveSeriaWhenFound() throws ParseException {
		Seria seria1 = new Seria("seria");
		when(seriaRepository.findByName(Mockito.anyString())).thenReturn(Arrays.asList(seria1));
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		BookInfo info = new BookInfo("test name", "archive.zip", "111.fb2", "seria", 1L, format.parse("2020-01-01"), false, "ru", new String[] {}, new AuthorInfo[] {}, 555);
		bookLoader.saveBook(info);
		
		verify(seriaRepository, times(0)).save(Mockito.any());
	}
	
	@Test
	public void TestSaveAuthorWhenNonFound() throws ParseException {
		when(authorRepository.findByFullName(Mockito.anyString())).thenReturn(new ArrayList<Author>());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		BookInfo info = new BookInfo("test name", "archive.zip", "111.fb2", "seria", 1L, format.parse("2020-01-01"), false, "ru", new String[] {}, new AuthorInfo[] {new AuthorInfo("John", "Smith", ""), new AuthorInfo("primary", "last", "middle")}, 555);
		bookLoader.saveBook(info);
		
		verify(authorRepository, times(2)).save(Mockito.any());
		verify(authorRepository, times(1)).save(new Author("Smith", "John", ""));
		verify(authorRepository, times(1)).save(new Author("last", "primary", "middle"));
	}

	@Test
	public void TestNonSaveAuthorWhenFound() throws ParseException {
		when(authorRepository.findByFullName(Mockito.anyString())).thenReturn(Arrays.asList(new Author("Smith", "John", "")));
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		BookInfo info = new BookInfo("test name", "archive.zip", "111.fb2", "seria", 1L, format.parse("2020-01-01"), false, "ru", new String[] {}, new AuthorInfo[] {new AuthorInfo("John", "Smith", "")}, 555);
		bookLoader.saveBook(info);
		
		verify(authorRepository, times(0)).save(Mockito.any());
	}
	
	@Test
	public void TestSaveBookWhenNonFound() throws ParseException {
		when(bookRepository.findByFilename(Mockito.anyString())).thenReturn(null);
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		BookInfo info = new BookInfo("test name", "archive.zip", "111.fb2", "seria", 1L, format.parse("2020-01-01"), false, "ru", new String[] {}, new AuthorInfo[] {new AuthorInfo("John", "Smith", "")}, 555);
		bookLoader.saveBook(info);

		verify(bookRepository, times(1)).save(Mockito.any());
		
		Book book = new Book();
		book.setTitle("test name");
		book.setArchiveName("archive.zip");
		book.setFilename("111.fb2");
		book.setSeria(new Seria("seria"));
		book.setSeriaNum(1L);
		book.setAdditionDate(format.parse("2020-01-01"));
		book.setIsDeleted(0);
		book.setLanguage("ru");
		
		HashSet<Author> authors = new HashSet<Author>();
		authors.add(new Author("Smith", "John", ""));
		book.setAuthors(authors);
		book.setSize(555);
		
		verify(bookRepository, times(1)).save(book);
	}

	@Test
	public void TestNonSaveBookWhenFound() throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Book book = new Book();
		when(bookRepository.findByFilename(Mockito.anyString())).thenReturn(book);
		
		BookInfo info = new BookInfo("test name", "archive.zip", "111.fb2", "seria", 1L, format.parse("2020-01-01"), false, "ru", new String[] {}, new AuthorInfo[] {new AuthorInfo("John", "Smith", "")}, 555);
		bookLoader.saveBook(info);

		verify(bookRepository, times(0)).save(Mockito.any());
	}
}
