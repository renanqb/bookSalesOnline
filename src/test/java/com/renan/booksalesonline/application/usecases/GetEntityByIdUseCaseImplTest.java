package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.DataQuery;
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
public class GetEntityByIdUseCaseImplTest {

    @Mock private DataQuery<Country> countryQuery;
    @Mock private RepositoryMediator mediator;
    @InjectMocks private GetEntityByIdUseCaseImpl getEntityByIdUseCase;

    @Test
    public void should_get_at_least_three_entities_from_use_case() throws NoSuchMethodException {

        var outputCountry = new Country(1, "country1", "gentilic1");

        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(anyInt())).thenReturn(outputCountry);

        var country = getEntityByIdUseCase.execute(Country.class, 1);
        assertThat(country).isEqualTo(outputCountry);
    }

    @Test
    public void should_not_find_entity_from_use_case() throws NoSuchMethodException {

        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(anyInt())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> getEntityByIdUseCase.execute(Country.class, 1));
    }
}
