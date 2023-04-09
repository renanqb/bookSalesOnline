package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.out.country.CountryCommand;
import com.renan.booksalesonline.application.ports.out.country.CountryQuery;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import com.renan.booksalesonline.domain.exceptions.UpdateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCountryUseCaseImplTest {

    @Mock private CountryQuery queryRepository;
    @Mock private CountryCommand commandRepository;
    @InjectMocks private UpdateCountryUseCaseImpl updateCountryUseCaseImpl;

    @Test
    public void should_update_a_country_successfully() {

        var mockedCountry = new Country(1, "name", "gentilic");
        when(queryRepository.getById(anyInt())).thenReturn(mockedCountry);
        when(commandRepository.save(any(Country.class))).thenReturn(mockedCountry);

        var country = updateCountryUseCaseImpl.execute(1, mockedCountry);
        assertThat(country).isEqualTo(mockedCountry);
    }

    @Test
    public void should_not_update_a_country_and_throws() {

        var mockedCountry = new Country(1, "name", "gentilic");
        when(queryRepository.getById(anyInt())).thenReturn(null);

        assertThrows(UpdateException.class, () -> {
            updateCountryUseCaseImpl.execute(1, mockedCountry);
        });
    }
}
