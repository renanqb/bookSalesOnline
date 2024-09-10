package com.renan.booksalesonline.tests.application.usecases.publisher;

import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.CreateEntityUseCase;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.application.usecases.publisher.CreatePublisherUseCaseImpl;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;
import com.renan.booksalesonline.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreatePublisherUseCaseImplTest {

    @Mock private DataQuery<Country> countryQuery;
    @Mock private RepositoryMediator repositoryMediator;
    @Mock private CreateEntityUseCase createEntityUseCase;
    @InjectMocks private CreatePublisherUseCaseImpl createPublisherUseCase;

    @Test
    public void should_create_a_publisher_whether_country_does_exists() throws NoSuchMethodException {

        var country = new Country(1, "name", "gentilic");
        var publisher = new Publisher(0, "name", "history", country);

        when(repositoryMediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(1)).thenReturn(country);
        when(createEntityUseCase.execute(Publisher.class, publisher)).thenReturn(publisher);

        var outputEntity = createPublisherUseCase.execute(publisher);
        assertThat(outputEntity).isEqualTo(publisher);
    }

    @Test
    public void should_not_create_a_publisher_whether_country_does_not_exists() throws NoSuchMethodException {

        var country = new Country(1, "name", "gentilic");
        var publisher = new Publisher(0, "name", "history", country);

        when(repositoryMediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(1)).thenReturn(null);

        assertThrows(ValidationException.class, () -> createPublisherUseCase.execute(publisher));
    }
}
