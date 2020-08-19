package com.example.demo.repo;

import com.example.demo.domain.Numbers;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA repository for basic CRUD db operations for Numbers entities
 */
public interface NumbersRepository extends CrudRepository<Numbers, Long> {
}
