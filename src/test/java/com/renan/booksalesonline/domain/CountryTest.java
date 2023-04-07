package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryTest {

    @Test
    public void should_create_a_country_domain_instance() {

        var expected = new CountryEntity(99, "Brazil", "Brazilian");
        assertThat(expected.getId()).isEqualTo(99);
        assertThat(expected.getName()).isEqualTo("Brazil");
        assertThat(expected.getNationality()).isEqualTo("Brazilian");

    }
}
