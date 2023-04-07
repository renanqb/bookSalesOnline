package com.renan.booksalesonline.adapters.controllers.v1.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryDtoTest {

    @Test
    public void should_create_a_country_dto_instance() {

        var expected = new CountryDto("Brazil", "Brazilian");
        assertThat(expected.getId()).isEqualTo(0);
        assertThat(expected.getName()).isEqualTo("Brazil");
        assertThat(expected.getNationality()).isEqualTo("Brazilian");
    }
}
