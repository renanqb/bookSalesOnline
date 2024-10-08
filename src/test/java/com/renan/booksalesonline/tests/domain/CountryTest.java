package com.renan.booksalesonline.tests.domain;

import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryTest {

    @Test
    public void should_create_a_default_country_domain_instance() {

        var country = new Country(0);
        assertThat((int)country.getId()).isEqualTo(0);
        assertThat(country.getName()).isEqualTo("");
        assertThat(country.getNationality()).isEqualTo("");
    }

    @Test
    public void should_create_a_country_domain_with_only_id_instance() {

        var country = new Country(99);
        assertThat((int)country.getId()).isEqualTo(99);
        assertThat(country.getName()).isEqualTo("");
        assertThat(country.getNationality()).isEqualTo("");
    }

    @Test
    public void should_create_a_country_domain_instance() {

        var country = new Country(99, "brazil", "brazilian");
        assertThat((int)country.getId()).isEqualTo(99);
        assertThat(country.getName()).isEqualTo("brazil");
        assertThat(country.getNationality()).isEqualTo("brazilian");
    }
}
