package com.renan.booksalesonline.application.mediators;

import com.renan.booksalesonline.application.ports.in.*;
import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UseCaseMediatorImplTest {

    private UseCaseMediator useCaseMediator;

    @BeforeAll
    public void init() {

        useCaseMediator = new UseCaseMediatorImpl(
                Mockito.mock(GetAllEntitiesUseCase.class),
                Mockito.mock(GetEntityByIdUseCase.class),
                Mockito.mock(CreateEntityUseCase.class),
                Mockito.mock(UpdateEntityUseCase.class),
                Mockito.mock(RemoveEntityUseCase.class)
        );
    }

    @Test
    public void should_get_a_country_get_all_countries_use_case() throws NoSuchMethodException {

        var useCase = useCaseMediator.get(GetAllEntitiesUseCase.class);

        assertThat(useCase).isNotNull();
        assertThat(useCase).isInstanceOf(GetAllEntitiesUseCase.class);
    }

    @Test
    public void should_get_an_no_such_method_exception() {

        var ex = assertThrows(
                NoSuchMethodException.class,
                () -> useCaseMediator.get(Object.class)
        );

        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).isEqualTo("There is no use case provider");
    }
}
