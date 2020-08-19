package com.example.demo.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Given the api is running,
     * when provided 1,2,3 as numbers param to the store endpoint,
     * then an id is returned
     */
    @Test
    void store_Success() throws Exception {
        mockMvc.perform(get("/store?numbers=1,2,3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
        ;
    }

    /**
     * Given the api is running,
     * when provided a non-numeric element in the numbers param to the store endpoint,
     * then an id is returned
     */
    @Test
    void store_NonNumericalElement() throws Exception {
        mockMvc.perform(get("/store?numbers=1,2,3,abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
        ;
    }

    /**
     * Given the api is running,
     * when provided an empty array in the numbers param to the store endpoint,
     * then an id is returned
     */
    @Test
    void store_EmptyArray() throws Exception {
        mockMvc.perform(get("/store?numbers="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
        ;
    }

    /**
     * Given the api is running,
     * when no numbers param is provided to the store endpoint,
     * then an error is returned
     */
    @Test
    void store_MissingNumbers() throws Exception {
        mockMvc.perform(get("/store"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Required String parameter 'numbers' is not present"))
        ;
    }

    /**
     * Given the api is running and 1,2,3 is provided as numbers param to the store endpoint,
     * when the permutation endpoint is called with a valid id,
     * then 1,2,3 is returned in a random sequence
     */
    @Test
    void permutation_Success() throws Exception {
        MvcResult storeResult = mockMvc.perform(get("/store?numbers=1,2,3"))
                .andReturn();
        Map resp = new ObjectMapper().readValue(storeResult.getResponse().getContentAsString(), Map.class);
        mockMvc.perform(get("/permutation?id=" + resp.get("id")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$", hasItem(1)))
                .andExpect(jsonPath("$", hasItem(2)))
                .andExpect(jsonPath("$", hasItem(3)))
        ;
    }

    /**
     * Given the api is running and a non-numerical element is provided in the numbers param to the store endpoint,
     * when the permutation endpoint is called with a valid id,
     * then the non-numerical element is ignored and numbers are returned in a random sequence
     */
    @Test
    void permutation_NonNumericalElement() throws Exception {
        MvcResult storeResult = mockMvc.perform(get("/store?numbers=1,2,3,abc"))
                .andReturn();
        Map resp = new ObjectMapper().readValue(storeResult.getResponse().getContentAsString(), Map.class);
        mockMvc.perform(get("/permutation?id=" + resp.get("id")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$", hasItem(1)))
                .andExpect(jsonPath("$", hasItem(2)))
                .andExpect(jsonPath("$", hasItem(3)))
        ;
    }

    /**
     * Given the api is running and an empty array is provided as the numbers param to the store endpoint,
     * when the permutation endpoint is called with a valid id,
     * then an empty array is returned
     */
    @Test
    void permutation_EmptyArray() throws Exception {
        MvcResult storeResult = mockMvc.perform(get("/store?numbers="))
                .andReturn();
        Map resp = new ObjectMapper().readValue(storeResult.getResponse().getContentAsString(), Map.class);
        mockMvc.perform(get("/permutation?id=" + resp.get("id")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)))
        ;
    }

    /**
     * Given the api is running,
     * when the permutation endpoint is called with an invalid id,
     * then an empty array is returned
     */
    @Test
    void permutation_MissingId() throws Exception {
        mockMvc.perform(get("/permutation"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Required long parameter 'id' is not present"))
        ;
    }

    /**
     * Given the api is running,
     * when the permutation endpoint is called with a non-numerical id,
     * then an error is returned
     */
    @Test
    void permutation_IdNotNumber() throws Exception {
        mockMvc.perform(get("/permutation?id=abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid value [abc] for id"))
        ;
    }

    /**
     * Given the api is running,
     * when the permutation endpoint is called with an invalid id,
     * then an error is returned
     */
    @Test
    void permutation_InvalidId() throws Exception {
        mockMvc.perform(get("/permutation?id=123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Id not found: 123"))
        ;
    }
}