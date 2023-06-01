package com.renan.booksalesonline.adapters.repositories.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryEntityTest {

    @Test
    public void should_create_a_country_entity_instance() {

        var expected = new CountryEntity(99, "Brazil", "Brazilian");
        assertThat(expected.getId()).isEqualTo(99);
        assertThat(expected.getName()).isEqualTo("Brazil");
        assertThat(expected.getGentilic()).isEqualTo("Brazilian");

    }
}
