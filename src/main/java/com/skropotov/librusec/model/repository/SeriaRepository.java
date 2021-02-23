package com.skropotov.librusec.model.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.skropotov.librusec.model.Seria;

public interface SeriaRepository extends CrudRepository<Seria, Long> {
	List<Seria> findByNameContainingIgnoreCaseOrderByName(String Name);
	List<Seria> findByName(String Name);
}
