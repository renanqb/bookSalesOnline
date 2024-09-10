package com.renan.booksalesonline.tests.application.usecases;

import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.application.usecases.GetAllEntitiesUseCaseImpl;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllEntitiesUseCaseImplTest {

    @Mock private DataQuery<Country> countryQuery;
    @Mock private RepositoryMediator mediator;
    @InjectMocks private GetAllEntitiesUseCaseImpl getAllEntitiesUseCase;

    @Test
    public void should_get_at_least_three_entities_from_use_case() throws NoSuchMethodException {

        var mockedCountries = Arrays.asList(
                new Country(1, "country1", "gentilic1"),
                new Country(1, "country1", "gentilic1"),
                new Country(1, "country1", "gentilic1")
        );

        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getAll(anyInt(), anyInt())).thenReturn(mockedCountries);

        var countries = getAllEntitiesUseCase.execute(Country.class);
        assertThat(countries).isEqualTo(mockedCountries);
    }
}

