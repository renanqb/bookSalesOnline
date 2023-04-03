package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryEntityMapperTest {

    @Test
    public void should_return_null_when_country_entity_is_null() {

        assertThat(CountryEntityMapper.toDomain((CountryEntity) null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_country_entity_to_country_domain() {

        var entity = new CountryEntity(1, "name", "nationality");
        var expected = new Country(1, "name", "nationality");
        var actual = CountryEntityMapper.toDomain(entity);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_return_null_when_country_domain_is_null() {

        assertThat(CountryEntityMapper.fromDomain(null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_country_domain_to_country_entity() {

        var domain = new Country(1, "name", "nationality");
        var expected = new CountryEntity(1, "name", "nationality");
        var actual = CountryEntityMapper.fromDomain(domain);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_return_empty_list_when_domain_list_is_null() {

        assertThat(CountryEntityMapper.toDomain((List<CountryEntity>) null))
                .usingRecursiveComparison()
                .isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void should_parse_country_entities_to_countries() {

        var entities = Arrays.asList(
                new CountryEntity(1, "name1", "nationality1"),
                new CountryEntity(2, "name2", "nationality2"),
                new CountryEntity(3, "name3", "nationality3")
        );
        var expected = Arrays.asList(
                new Country(1, "name1", "nationality1"),
                new Country(2, "name2", "nationality2"),
                new Country(3, "name3", "nationality3")
        );
        var actual = CountryEntityMapper.toDomain(entities);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
