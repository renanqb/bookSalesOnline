package com.renan.booksalesonline.tests.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.CountryRepository;
import com.renan.booksalesonline.adapters.repositories.data.CountryData;
import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryRepositoryTest {

    @Mock private CountryData countryData;
    @InjectMocks private CountryRepository countryRepository;

    @Test
    public void should_get_all_country_entities() {

        var countryEntities = Arrays.asList(
                new CountryEntity(1, "name1", "gentilic1"),
                new CountryEntity(2, "name2", "gentilic2"),
                new CountryEntity(3, "name3", "gentilic3")
        );
        when(countryData.findAll()).thenReturn(countryEntities);

        var expectedCountries = Arrays.asList(
                new Country(1, "name1", "gentilic1"),
                new Country(2, "name2", "gentilic2"),
                new Country(3, "name3", "gentilic3")
        );

        var countries = countryRepository.getAll();

        for (int i = 0; i < 3; i++) {
            var expected = expectedCountries.get(i);
            var actual = countries.get(i);

            assertThat((int)actual.getId()).isEqualTo((int)expected.getId());
            assertThat(actual.getName()).isEqualTo(expected.getName());
            assertThat(actual.getGentilic()).isEqualTo(expected.getGentilic());
        }
    }

    @Test
    public void should_get_by_id_one_country_entity() {

        var mockedCountryEntity = new CountryEntity(1, "name1", "gentilic1");
        when(countryData.findById(anyInt())).thenReturn(Optional.of(mockedCountryEntity));

        var expected = new Country(1, "name1", "gentilic1");
        var country = countryRepository.getById(1);

        assertThat((int)country.getId()).isEqualTo((int)expected.getId());
        assertThat(country.getName()).isEqualTo(expected.getName());
        assertThat(country.getGentilic()).isEqualTo(expected.getGentilic());
    }

    @Test
    public void should_create_a_country_entity() {

        var mockedCountryEntity = new CountryEntity(1, "name1", "gentilic1");
        when(countryData.save(any(CountryEntity.class))).thenReturn(mockedCountryEntity);

        var expected = new Country(1, "name1", "gentilic1");
        var country = countryRepository.save(expected);

        assertThat((int)country.getId()).isEqualTo((int)expected.getId());
        assertThat(country.getName()).isEqualTo(expected.getName());
        assertThat(country.getGentilic()).isEqualTo(expected.getGentilic());
    }

    @Test
    public void should_remove_a_country_entity() {

        doNothing().when(countryData).delete(any(CountryEntity.class));

        assertDoesNotThrow(() -> {
            var country = new Country(1, "name1", "gentilic1");
            countryRepository.remove(country);
        });
    }
}
