package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.out.country.CountryQuery;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllCountriesUseCaseImplTest {

    @Mock private CountryQuery repository;
    @InjectMocks private GetAllCountriesUseCaseImpl getAllCountriesUseCase;

    @Test
    public void should_get_at_least_three_countries_from_use_case() {

        var mockedCountries = Arrays.asList(
                new Country(1, "country1", "gentilic1"),
                new Country(1, "country1", "gentilic1"),
                new Country(1, "country1", "gentilic1")
        );

        when(repository.getAll()).thenReturn(mockedCountries);

        var countries = getAllCountriesUseCase.execute();
        assertThat(countries).isEqualTo(mockedCountries);
    }
}
