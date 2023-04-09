package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.out.country.CountryQuery;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCountryByIdUseCaseImplTest {

    @Mock private CountryQuery repository;
    @InjectMocks private GetCountryByIdUseCaseImpl getCountryByIdUseCaseImpl;

    @Test
    public void should_find_country_from_use_case() {

        var mockedCountry = new Country(1, "country1", "gentilic1");
        when(repository.getById(anyInt())).thenReturn(mockedCountry);

        var country = getCountryByIdUseCaseImpl.execute(1);
        assertThat(country).isEqualTo(mockedCountry);
    }

    @Test
    public void should_not_find_country_from_use_case() {

        when(repository.getById(anyInt())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            getCountryByIdUseCaseImpl.execute(0);
        });
    }
}
