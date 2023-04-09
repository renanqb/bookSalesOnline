package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.out.country.CountryCommand;
import com.renan.booksalesonline.application.ports.out.country.CountryQuery;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.RemoveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveCountryUseCaseImplTest {

    @Mock private CountryQuery queryRepository;
    @Mock private CountryCommand commandRepository;
    @InjectMocks private RemoveCountryUseCaseImpl removeCountryUseCaseImpl;

    @Test
    public void should_remove_a_country_successfully() {

        var mockedCountry = new Country(1, "name", "gentilic");
        when(queryRepository.getById(anyInt())).thenReturn(mockedCountry);
        doNothing().when(commandRepository).remove(any(Country.class));

        assertDoesNotThrow(() -> {
            removeCountryUseCaseImpl.execute(1);
        });
    }

    @Test
    public void should_not_remove_a_country_and_throws() {

        when(queryRepository.getById(anyInt())).thenReturn(null);

        assertThrows(RemoveException.class, () -> {
            removeCountryUseCaseImpl.execute(1);
        });
    }
}
