package com.renan.booksalesonline.tests.adapters.controllers.v1.model;

import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublisherDto;
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
    public void should_create_a_default_publisher_dto_instance() {

        var publisherDto = new PublisherDto();
        assertThat(publisherDto.getId()).isEqualTo(0);
        assertThat(publisherDto.getName()).isNull();
        assertThat(publisherDto.getHistory()).isNull();
        assertThat(publisherDto.getCountry()).isNull();
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
