package com.example.demo.service;

import com.example.demo.domain.Numbers;
import com.example.demo.error.RecordNotFoundException;
import com.example.demo.repo.NumbersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DemoServiceTest {
    @InjectMocks
    private DemoService service;
    @Mock
    private NumbersRepository numbersRepository;

    @BeforeEach
    void setUp() {
        service = new DemoService(numbersRepository);
    }

    /**
     * Given an in-memory storage for the numbers,
     * when the store method is called with a list of numbers,
     * then an id of the stored numbers is returned
     */
    @Test
    void store_Success() {
        ArgumentCaptor<Numbers> numbersArgumentCaptor = ArgumentCaptor.forClass(Numbers.class);
        when(numbersRepository.save(numbersArgumentCaptor.capture())).thenAnswer(iom -> {
            Numbers entity = (Numbers) iom.getArguments()[0];
            entity.setId(123L);
            return entity;
        });

        assertEquals(123, service.store(Collections.singletonList(19L)));
        assertArrayEquals(Collections.singletonList(19L).toArray(), numbersArgumentCaptor.getValue().getNumbers().toArray());
    }

    /**
     * Given an in-memory storage for the numbers and a list of numbers are already stored,
     * when the permutation method is called with a valid id,
     * then a random list of the numbers is returned
     */
    @Test
    void permutation_Success() {
        List<Long> numbers = LongStream.of(1, 2, 3, 4, 5).boxed().collect(Collectors.toList());

        when(numbersRepository.findById(123L)).thenReturn(Optional.of(Numbers.builder().numbers(numbers).build()));

        List<Long> permutation = service.permutation(123L);

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
        when(numbersRepository.findById(-1L)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(RecordNotFoundException.class, () -> service.permutation(-1));
    }
}