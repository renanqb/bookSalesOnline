package com.renan.booksalesonline.tests.application.usecases;

import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.usecases.CreateEntityUseCaseImpl;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateEntityUseCaseImplTest {

    @Mock private DataCommand<Country> countryCommand;
    @Mock private RepositoryMediator mediator;
    @InjectMocks private CreateEntityUseCaseImpl createEntityUseCaseImpl;

    @Test
    public void should_create_an_entity_successfully() throws NoSuchMethodException {

        var inputEntity = new Country(0, "name", "gentilic");

        when (mediator.getCommand(Country.class))
                .thenReturn(countryCommand);

        when (countryCommand.save(inputEntity)).then(r -> {
            inputEntity.setId(1);
            return inputEntity;
        });

        var outputEntity = createEntityUseCaseImpl.execute(Country.class, inputEntity);
        assertThat(outputEntity).isEqualTo(inputEntity);
    }
}
