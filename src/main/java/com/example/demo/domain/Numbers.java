package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * JPA entity to store lists of numbers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Numbers {
    /**
     * Auto-incremented id
     */
    @Id
    @GeneratedValue
    private Long id;
    // @ElementCollection will store numbers in a separate second table
    // Alternatively, we could also store numbers as a CLOB of comma separated numbers and convert them using
    // a custom javax.persistence.Converter class
    // - this way we'd only have one table and no need for a join (or two selects) when fetching the list of numbers:
    @ElementCollection(fetch = FetchType.EAGER)
    // fetch mode set to join will run a select with a join instead of running two selects
    @Fetch(FetchMode.JOIN)
    private List<Long> numbers;
}
