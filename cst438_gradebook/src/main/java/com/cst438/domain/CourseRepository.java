package com.cst438.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository <Course, Integer> {
	Optional<Course> findById(int id);
}
