package com.skropotov.librusec.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.skropotov.librusec.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
	List<Author> findByFullNameContainingIgnoreCase(String FullName);
	List<Author> findByFullName(String FullName);
}
