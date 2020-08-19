package com.example.demo.web;

import com.example.demo.model.StoreNumbersResponse;
import com.example.demo.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * REST api endpoints.
 */
@RestController
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    /**
     * REST endpoint to store list of numbers
     * @param numbers
     * @return
     */
    @GetMapping("/store")
    public StoreNumbersResponse store(@RequestParam("numbers") String numbers) {
        List<Long> nums = Arrays.stream(numbers.split(","))
                .map(String::trim)
                .map(str -> {
                    try {
                        return Long.parseLong(str);
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        long id = demoService.store(nums);
        return StoreNumbersResponse.builder()
                .id(id)
                .build();
    }

    /**
     * REST endpoint to get random permutation of stored numbers
     * @param id
     * @return
     */
    @GetMapping("/permutation")
    public List<Long> permutation(@RequestParam("id") long id) {
        return demoService.permutation(id);
    }
}
