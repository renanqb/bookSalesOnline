package com.renan.booksalesonline.tests.application.usecases;

import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.application.usecases.UpdateEntityUseCaseImpl;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.UpdateException;
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
public class UpdateEntityUseCaseImplTest {

    @Mock private DataQuery<Country> countryQuery;
    @Mock private DataCommand<Country> countryCommand;
    @Mock private RepositoryMediator mediator;
    @InjectMocks private UpdateEntityUseCaseImpl updateEntityUseCaseImpl;

    @Test
    public void should_update_an_entity_successfully() throws NoSuchMethodException {

        var inputEntity = new Country(1, "name", "gentilic");

        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when (mediator.getCommand(Country.class)).thenReturn(countryCommand);
        when(countryQuery.getById(anyInt())).thenReturn(inputEntity);
        when (countryCommand.save(inputEntity)).then(r -> {
            inputEntity.setId(1);
            return inputEntity;
        });

        var outputEntity = updateEntityUseCaseImpl.execute(Country.class, inputEntity, -1);
        assertThat(outputEntity).isEqualTo(inputEntity);
    }

    @Test
    public void should_not_found_an_entity_for_update() throws NoSuchMethodException {

        var inputEntity = new Country(2, "name", "gentilic");

        when(mediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(anyInt())).thenReturn(null);

        assertThrows(UpdateException.class, () ->
                updateEntityUseCaseImpl.execute(Country.class, inputEntity, 2)
        );
    }
}
