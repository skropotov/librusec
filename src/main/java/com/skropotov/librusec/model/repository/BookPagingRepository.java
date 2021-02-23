package com.skropotov.librusec.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.skropotov.librusec.model.Book;
import com.skropotov.librusec.model.Genre;

public interface BookPagingRepository extends PagingAndSortingRepository<Book, Long>{
	Page<Book> findByGenres(Genre genre, Pageable pageable);
}
