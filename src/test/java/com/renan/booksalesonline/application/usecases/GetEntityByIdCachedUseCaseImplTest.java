package com.renan.booksalesonline.application.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.application.ports.out.base.RedisCache;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
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
public class GetEntityByIdCachedUseCaseImplTest {

    @Mock private DataQuery<Country> countryQuery;
    @Mock private RepositoryMediator mediator;
    @Mock private RedisCache redisCache;
    @InjectMocks private GetEntityByIdCachedUseCaseImpl getEntityByIdUseCase;

    @Test
    public void should_find_entity_ignoring_cache_in_use_case()
            throws NoSuchMethodException, JsonProcessingException {

        var outputCountry = new Country(1, "country1", "gentilic1");

        when(redisCache.getById(any(), anyInt())).thenReturn(null);
        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(anyInt())).thenReturn(outputCountry);

        var country = getEntityByIdUseCase.execute(Country.class, 1);
        assertThat(country).isEqualTo(outputCountry);
    }

    @Test
    public void should_not_find_entity_ignoring_cache_in_use_case()
            throws NoSuchMethodException, JsonProcessingException {

        when(redisCache.getById(any(), anyInt())).thenReturn(null);
        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(anyInt())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> getEntityByIdUseCase.execute(Country.class, 1));
    }

    @Test
    public void should_find_entity_from_cache_in_use_case()
            throws NoSuchMethodException, JsonProcessingException {

        var outputCountry = new Country(1, "country1", "gentilic1");

        when(redisCache.getById(any(), anyInt())).thenReturn(outputCountry);

        var country = getEntityByIdUseCase.execute(Country.class, 1);
        assertThat(country).isEqualTo(outputCountry);
    }
}
