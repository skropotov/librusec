package com.skropotov.librusec.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.skropotov.librusec.model.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long> {
	List<Genre> findByNameContainingIgnoreCase(String Name);
	List<Genre> findByParent_Id(Long id);
	List<Genre> findByParent(Genre genre);
	List<Genre> findByFb2code(String fb2code);
}
