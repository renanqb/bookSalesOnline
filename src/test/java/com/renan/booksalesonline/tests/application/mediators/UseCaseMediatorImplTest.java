package com.renan.booksalesonline.tests.application.mediators;

import com.renan.booksalesonline.application.mediators.UseCaseMediatorImpl;
import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.usecases.*;
import com.renan.booksalesonline.application.ports.in.usecases.country.GetPublishersByCountryUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.country.RemoveCountryUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.GetAllPublicationImagesUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.CreatePublicationImageUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.UpdatePublicationImageUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.CreatePublisherUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.RemovePublisherUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.UpdatePublisherUseCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UseCaseMediatorImplTest {

    private UseCaseMediator useCaseMediator;

    @BeforeAll
    public void init() {

        useCaseMediator = new UseCaseMediatorImpl(
                mock(GetAllEntitiesUseCase.class),
                mock(GetEntityByIdUseCase.class),
                mock(CreateEntityUseCase.class),
                mock(UpdateEntityUseCase.class),
                mock(RemoveEntityUseCase.class),
                mock(RemoveCountryUseCase.class),
                mock(CreatePublisherUseCase.class),
                mock(UpdatePublisherUseCase.class),
                mock(RemovePublisherUseCase.class),
                mock(GetPublishersByCountryUseCase.class),
                mock(GetAllPublicationImagesUseCase.class),
                mock(CreatePublicationImageUseCase.class),
                mock(UpdatePublicationImageUseCase.class)
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
