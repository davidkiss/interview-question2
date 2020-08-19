package com.example.demo.service;

import com.example.demo.error.RecordNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Business logic for storing and returning a permutation of a list of numbers
 */
@Service
public class DemoService {
    private List<List<Long>> db = new ArrayList<>();
    // lock is used to support concurrent HTTP requests:
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Stores a list of numbers in the in-memory <code>db</code> list
     * @param numbers list of longs
     * @return id of stored list, where id is the size of <code>db</code> after the add
     */
    public long store(List<Long> numbers) {
        lock.writeLock().lock();
        try {
            db.add(numbers);
            return db.size();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     *
     * @param id <code>id - 1</code> is the index of list of numbers in <code>db</code>
     * @return Random permutation of a list of numbers stored previously by calling <code>store()</code> method
     */
    public List<Long> permutation(long id) {
        lock.readLock().lock();
        try {
            if (id > db.size() || id < 0) {
                throw new RecordNotFoundException("Id not found: " + id);
            }

            List<Long> numbers = new ArrayList<>(db.get((int) id - 1));
            Collections.shuffle(numbers);
            return numbers;
        } finally {
            lock.readLock().unlock();
        }
    }
}
