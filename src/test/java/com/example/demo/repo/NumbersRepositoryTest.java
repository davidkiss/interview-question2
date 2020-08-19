package com.example.demo.repo;

import com.example.demo.domain.Numbers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NumbersRepositoryTest {
    @Autowired
    private NumbersRepository numbersRepository;
    @Autowired
    private TestEntityManager entityManager;

    /**
     * Given the app is configured to use H2 database
     * When a Numbers entity is saved
     * Then it will have an auto-generated id assigned
     */
    @Test
    void save_Success() {
        Numbers numbers = numbersRepository.save(Numbers.builder()
            .numbers(Collections.singletonList(79L))
            .build());

        assertNotNull(numbers);
        assertNotNull(numbers.getId());
        assertArrayEquals(new Long[] { 79L }, numbers.getNumbers().toArray());
    }

    /**
     * Given the app is configured to use H2 database and an entity is already saved
     * When calling <code>findById()</code> method in the NumbersRepository
     * Then it will return the saved entity with matching id
     */
    @Test
    void findById_Success() {
        Numbers numbers = entityManager.persist(Numbers.builder()
            .numbers(Collections.singletonList(79L))
            .build());

        Optional<Numbers> result = numbersRepository.findById(numbers.getId());

        assertTrue(result.isPresent());
        assertEquals(numbers.getId(), result.get().getId());
        assertArrayEquals(new Long[] { 79L }, result.get().getNumbers().toArray());
    }

    /**
     * Given the app is configured to use H2 database
     * When calling <code>findById()</code> method in the NumbersRepository with an invalid id
     * Then it will not return any entity
     */
    @Test
    void findById_InvalidId() {
        assertFalse(numbersRepository.findById(-123L).isPresent());
    }
}