package com.renan.booksalesonline.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.model.CountryDto;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryDtoMapperTest {

    @Test
    public void should_parse_country_dto_to_country_domain() {

        var dto = new CountryDto("name", "gentilic");
        dto.setId(1);

        var expected = new Country(1, "name", "gentilic");
        var actual = CountryDtoMapper.toDomain(dto);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_parse_country_domain_to_country_entity() {

        var domain = new Country(1, "name", "gentilic");
        var expected = new CountryDto("name", "gentilic");
        expected.setId(1);
        var actual = CountryDtoMapper.fromDomain(domain);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_parse_country_entities_to_countries() {

        var entities = Arrays.asList(
                new Country(1, "name1", "nationality1"),
                new Country(2, "name2", "nationality2"),
                new Country(3, "name3", "nationality3")
        );
        var expected = Arrays.asList(
                new CountryDto("name1", "nationality1"),
                new CountryDto("name2", "nationality2"),
                new CountryDto("name3", "nationality3")
        );
        expected.get(0).setId(1);
        expected.get(1).setId(2);
        expected.get(2).setId(3);

        var actual = CountryDtoMapper.fromDomain(entities);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
