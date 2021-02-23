package com.skropotov.librusec.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.skropotov.librusec.model.Author;
import com.skropotov.librusec.model.Book;
import com.skropotov.librusec.model.Genre;
import com.skropotov.librusec.model.Seria;

public interface BookRepository extends CrudRepository<Book, Long> {
	List<Book> findBySeriaOrderBySeriaNumAsc(Seria seria);
	List<Book> findBySeriaOrderBySeriaNumDesc(Seria seria);
	List<Book> findBySeriaOrderByAdditionDateAsc(Seria seria);
	List<Book> findBySeriaOrderByAdditionDateDesc(Seria seria);
	
	List<Book> findBySeria_NameContainingIgnoreCase(String name);

	List<Book> findByGenres(Genre genre);
	List<Book> findByGenresOrderByAdditionDateAsc(Genre genre);
	List<Book> findByGenresOrderByAdditionDateDesc(Genre genre);
	List<Book> findByGenresOrderBySeria_NameAscSeriaNumAsc(Genre genre);
	List<Book> findByGenresOrderBySeria_NameDescSeriaNumAsc(Genre genre);
	
	List<Book> findByAuthors(Author author);
	List<Book> findByAuthorsOrderByAdditionDateAsc(Author author);
	List<Book> findByAuthorsOrderByAdditionDateDesc(Author author);
	List<Book> findByAuthorsOrderBySeria_NameAscSeriaNumAsc(Author author);
	List<Book> findByAuthorsOrderBySeria_NameDescSeriaNumAsc(Author author);
	
	List<Book> findByAuthors_FullNameContainingIgnoreCase(String fullName);

	List<Book> findByTitleContainingIgnoreCase(String title);
	Book findByFilename(String filename);
}
