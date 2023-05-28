package com.renan.booksalesonline.application.mediators;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryMediatorImplTest {

    private DataQuery<Country> countryQuery;
    private DataCommand<Country> countryCommand;
    private RepositoryMediator repositoryMediator;

    @BeforeAll
    @SuppressWarnings("unchecked")
    public void init() {

        countryQuery = (DataQuery<Country>) mock(DataQuery.class);
        countryCommand = (DataCommand<Country>) mock(DataCommand.class);
        repositoryMediator = new RepositoryMediatorImpl(
                countryQuery, countryCommand, null,null);
    }

    @Test
    public void should_get_an_data_query_successfully() throws NoSuchMethodException {

        var actualQuery =
                repositoryMediator.getQuery(Country.class);

        assertThat(actualQuery).isNotNull();
        assertThat(actualQuery).isInstanceOf(countryQuery.getClass());
    }

    @Test
    public void should_get_an_data_command_successfully() throws NoSuchMethodException {

        var actualCommand =
                repositoryMediator.getCommand(Country.class);

        assertThat(actualCommand).isNotNull();
        assertThat(actualCommand).isInstanceOf(countryCommand.getClass());
    }

    @Test
    public void should_not_find_data_query_and_receive_no_such_method_exception() {

        assertThrows(NoSuchMethodException.class, () -> repositoryMediator.getQuery(Object.class));
    }

    @Test
    public void should_not_find_data_command_and_receive_no_such_method_exception() {

        assertThrows(NoSuchMethodException.class, () -> repositoryMediator.getCommand(Object.class));
    }
}
