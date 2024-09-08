package com.renan.booksalesonline.tests.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import com.renan.booksalesonline.adapters.repositories.entities.PublisherEntity;
import com.renan.booksalesonline.adapters.repositories.mappers.PublisherEntityMapper;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PublisherEntityMapperTest {

    @Test
    public void should_return_null_when_publisher_entity_is_null() {

        Assertions.assertThat(PublisherEntityMapper.toDomain((PublisherEntity) null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_publisher_entity_to_publisher_domain() {

        var countryEntity = new CountryEntity(1, "name", "gentilic");
        var publisherEntity = new PublisherEntity(1, "name", "history", countryEntity);

        var country = new Country(1, "name", "gentilic");
        var expectedPublisher = new Publisher(1, "name", "history", country);
        var actualPublisher = PublisherEntityMapper.toDomain(publisherEntity);

        assertThat(actualPublisher)
                .usingRecursiveComparison()
                .isEqualTo(expectedPublisher);
    }

    @Test
    public void should_return_null_when_publisher_domain_is_null() {

        assertThat(PublisherEntityMapper.fromDomain(null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_publisher_domain_to_publisher_entity() {

        var country = new Country(1, "name", "gentilic");
        var publisher = new Publisher(1, "name", "history", country);
        var countryEntity = new CountryEntity(1, "name", "gentilic");
        var expectedPublisherEntity = new PublisherEntity(1, "name", "history", countryEntity);
        var actualPublisherEntity = PublisherEntityMapper.fromDomain(publisher);

        assertThat(actualPublisherEntity)
                .usingRecursiveComparison()
                .isEqualTo(expectedPublisherEntity);
    }

    @Test
    public void should_return_publisher_empty_list_when_domain_list_is_null() {

        assertThat(PublisherEntityMapper.toDomain((List<PublisherEntity>) null))
                .usingRecursiveComparison()
                .isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void should_parse_publisher_entities_to_publisher_domains() {

        var countryEntity = new CountryEntity(1, "name", "gentilic");
        var entities = Arrays.asList(
                new PublisherEntity(1, "name1", "history1", countryEntity),
                new PublisherEntity(2, "name2", "history2", countryEntity),
                new PublisherEntity(3, "name3", "history3", countryEntity)
        );

        var country = new Country(1, "name", "gentilic");
        var expected = Arrays.asList(
                new Publisher(1, "name1", "history1", country),
                new Publisher(2, "name2", "history2", country),
                new Publisher(3, "name3", "history3", country)
        );
        var actual = PublisherEntityMapper.toDomain(entities);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
