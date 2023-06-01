package com.renan.booksalesonline.adapters.controllers.v1.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryDtoTest {

    @Test
    public void should_create_a_country_dto_instance() {

        var countryDto = new CountryDto("Brazil", "Brazilian");
        assertThat(countryDto.getId()).isEqualTo(0);
        assertThat(countryDto.getName()).isEqualTo("Brazil");
        assertThat(countryDto.getGentilic()).isEqualTo("Brazilian");
    }
}
