package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.out.country.CountryCommand;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCountryUseCaseImplTest {

    @Mock private CountryCommand repository;
    @InjectMocks private CreateCountryUseCaseImpl createCountryUseCaseImpl;

    @Test
    public void should_create_a_country_successfully() {

        var mockedCountry = new Country(1, "name", "gentilic");
        when(repository.save(any(Country.class))).thenReturn(mockedCountry);

        var country = createCountryUseCaseImpl.execute(mockedCountry);
        assertThat(country).isEqualTo(mockedCountry);
    }
}
