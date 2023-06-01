package com.renan.booksalesonline.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublisherDto;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class PublisherDtoMapperTest {

    @Test
    public void should_parse_publisher_dto_to_publisher_domain() {

        var countryDto = new CountryDto("name", "gentilic");
        countryDto.setId(1);
        var publisherDto = new PublisherDto("name", "history", countryDto);
        publisherDto.setId(1);

        var country = new Country(1);
        var expected = new Publisher(1, "name", "history", country);
        var actual = PublisherDtoMapper.toDomain(publisherDto);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_parse_publisher_domain_to_publisher_entity() {

        var country = new Country(1, "name", "gentilic");
        var publisher = new Publisher(1, "name", "history", country);
        var countryDto = new CountryDto("name", "gentilic");
        countryDto.setId(1);
        var expected = new PublisherDto("name", "history", countryDto);
        expected.setId(1);
        var actual = PublisherDtoMapper.fromDomain(publisher);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_parse_publisher_entities_to_countries() {

        var country = new Country(1, "name", "gentilic");
        var entities = Arrays.asList(
                new Publisher(1, "name1", "history1", country),
                new Publisher(2, "name2", "history2", country),
                new Publisher(3, "name3", "history3", country)
        );

        var countryDto = new CountryDto("name", "gentilic");
        countryDto.setId(1);
        var expected = new PublisherDto[]{
                new PublisherDto("name1", "history1", countryDto),
                new PublisherDto("name2", "history2", countryDto),
                new PublisherDto("name3", "history3", countryDto)
        };
        expected[0].setId(1);
        expected[1].setId(2);
        expected[2].setId(3);

        var actual = PublisherDtoMapper.fromDomain(entities);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
