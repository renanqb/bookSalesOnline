package com.renan.booksalesonline.tests.application.usecases;

import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.application.usecases.RemoveEntityUseCaseImpl;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.RemoveException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveEntityUseCaseImplTest {

    @Mock private DataQuery<Country> countryQuery;
    @Mock private DataCommand<Country> countryCommand;
    @Mock private RepositoryMediator mediator;
    @InjectMocks private RemoveEntityUseCaseImpl removeEntityUseCase;

    @Test
    public void should_remove_an_entity_use_case() throws NoSuchMethodException {

        var inputEntity = new Country(1, "name", "gentilic");

        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(mediator.getCommand(Country.class)).thenReturn(countryCommand);
        when(countryQuery.getById(anyInt())).thenReturn(inputEntity);
        doNothing().when(countryCommand).remove(any(Country.class));

        assertDoesNotThrow(() -> {
            removeEntityUseCase.execute(Country.class, 1);
        });
    }

    @Test
    public void should_not_find_entity_on_remove_use_case() throws NoSuchMethodException {

        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(mediator.getCommand(Country.class)).thenReturn(countryCommand);
        when(countryQuery.getById(anyInt())).thenReturn(null);

        assertThrows(RemoveException.class, () -> removeEntityUseCase.execute(Country.class, 1));
    }
}
