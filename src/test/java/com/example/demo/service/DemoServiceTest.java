package com.example.demo.service;

import com.example.demo.error.RecordNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DemoServiceTest {
    private DemoService service;

    @BeforeEach
    void setUp() {
        service = new DemoService();
    }

    /**
     * Given an in-memory storage for the numbers,
     * when the store method is called with a list of numbers,
     * then an id of the stored numbers is returned
     */
    @Test
    void store_Success() {
        assertEquals(1, service.store(Collections.emptyList()));
        assertEquals(2, service.store(Collections.singletonList(19L)));
    }

    /**
     * Given an in-memory storage for the numbers and a list of numbers are already stored,
     * when the permutation method is called with a valid id,
     * then a random list of the numbers is returned
     */
    @Test
    void permutation_Success() {
        List<Long> numbers = LongStream.of(1, 2, 3, 4, 5).boxed().collect(Collectors.toList());
        long id = service.store(numbers);

        List<Long> permutation = service.permutation(id);
        Assertions.assertAll("should return different values than original numbers",
                () -> assertEquals(5, permutation.size()),
                () -> assertTrue(permutation.contains(1L)),
                () -> assertTrue(permutation.contains(2L)),
                () -> assertTrue(permutation.contains(3L)),
                () -> assertTrue(permutation.contains(4L)),
                () -> assertTrue(permutation.contains(5L))
        );
    }

    /**
     * Given an in-memory storage for the numbers and a list of numbers are already stored,
     * when the permutation method is called with an invalid id,
     * then an exception is raised
     */
    @Test
    void permutation_InvalidId() {
        Assertions.assertThrows(RecordNotFoundException.class, () -> service.permutation(-1));
    }
}