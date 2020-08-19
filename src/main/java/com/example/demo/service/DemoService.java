package com.example.demo.service;

import com.example.demo.domain.Numbers;
import com.example.demo.error.RecordNotFoundException;
import com.example.demo.repo.NumbersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Business logic for storing and returning a permutation of a list of numbers
 */
@Service
// wraps all calls to both store() and permutation() methods in a db transaction:
@Transactional
public class DemoService {
    private final NumbersRepository numbersRepository;

    public DemoService(NumbersRepository numbersRepository) {
        this.numbersRepository = numbersRepository;
    }

    /**
     * Stores a list of numbers in the H2 db
     * @param numbers list of longs
     * @return id of Numbers entity storing the list of numbers
     */
    public long store(List<Long> numbers) {
        Numbers result = numbersRepository.save(Numbers.builder()
                .numbers(numbers)
                .build());
        return result.getId();
    }

    /**
     *
     * @param id id of Numbers entity that stores the list of numbers
     * @return Random permutation of a list of numbers stored previously by calling <code>store()</code> method
     */
    // marks transaction as read-only for performance reasons:
    @Transactional(readOnly = true)
    public List<Long> permutation(long id) {
        Optional<Numbers> maybeNumbers = numbersRepository.findById(id);
        if (!maybeNumbers.isPresent()) {
            throw new RecordNotFoundException("Id not found: " + id);
        }

        List<Long> numbers = new ArrayList<>(maybeNumbers.get().getNumbers());
        Collections.shuffle(numbers);
        return numbers;
    }
}
