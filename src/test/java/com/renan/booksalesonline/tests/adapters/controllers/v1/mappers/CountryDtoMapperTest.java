package com.renan.booksalesonline.tests.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.CountryDtoMapper;
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
                new Country(1, "name1", "gentilic1"),
                new Country(2, "name2", "gentilic2"),
                new Country(3, "name3", "gentilic3")
        );
        var expected = new CountryDto[] {
                new CountryDto("name1", "gentilic1"),
                new CountryDto("name2", "gentilic2"),
                new CountryDto("name3", "gentilic3")
        };
        expected[0].setId(1);
        expected[1].setId(2);
        expected[2].setId(3);

        var actual = CountryDtoMapper.fromDomain(entities);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
