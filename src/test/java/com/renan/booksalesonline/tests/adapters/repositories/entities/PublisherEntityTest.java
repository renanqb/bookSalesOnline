package com.renan.booksalesonline.tests.adapters.repositories.entities;

import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import com.renan.booksalesonline.adapters.repositories.entities.PublisherEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PublisherEntityTest {

    @Test
    public void should_create_a_publisher_entity_instance() {

        var countryEntity = new CountryEntity(1, "brazil", "");
        var expected = new PublisherEntity(99, "publisher", "history", countryEntity);
        assertThat(expected.getId()).isEqualTo(99);
        assertThat(expected.getName()).isEqualTo("publisher");
        assertThat(expected.getHistory()).isEqualTo("history");
        assertThat(expected.getCountry()).isNotNull();
    }

    @Test
    public void should_create_a_default_publisher_entity_instance() {

        var expected = new PublisherEntity();
        assertThat(expected.getId()).isEqualTo(0);
        assertThat(expected.getName()).isNull();
        assertThat(expected.getHistory()).isNull();
        assertThat(expected.getCountry()).isNull();
    }
}
