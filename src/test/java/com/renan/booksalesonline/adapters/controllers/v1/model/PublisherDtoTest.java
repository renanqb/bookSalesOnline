package com.renan.booksalesonline.adapters.controllers.v1.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PublisherDtoTest {

    @Test
    public void should_create_a_publisher_dto_instance() {

        var countryDto = new CountryDto("brazil", "brazilian");
        var publisherDto = new PublisherDto("publisher A", "history", countryDto);
        assertThat(publisherDto.getId()).isEqualTo(0);
        assertThat(publisherDto.getName()).isEqualTo("publisher A");
        assertThat(publisherDto.getHistory()).isEqualTo("history");
        assertThat(publisherDto.getCountry()).isNotNull();
    }

    @Test
    public void should_create_a_publisher_dto_instance_without_country() {

        var publisherDto = new PublisherDto("publisher A", "history", null);
        assertThat(publisherDto.getId()).isEqualTo(0);
        assertThat(publisherDto.getName()).isEqualTo("publisher A");
        assertThat(publisherDto.getHistory()).isEqualTo("history");
        assertThat(publisherDto.getCountry()).isNull();
    }
}
