package com.renan.booksalesonline.tests.application.usecases.publisher;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.UpdateEntityUseCase;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.application.usecases.publisher.UpdatePublisherUseCaseImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdatePublisherUseCaseImplTest {

    @Mock private DataQuery<Country> countryQuery;
    @Mock private RepositoryMediator repositoryMediator;
    @Mock private UpdateEntityUseCase updateEntityUseCase;
    @InjectMocks private UpdatePublisherUseCaseImpl updatePublisherUseCase;

    @Test
    public void should_update_a_publisher_whether_country_does_exists() throws NoSuchMethodException {

        var country = new Country(1, "name", "gentilic");
        var publisher = new Publisher(1, "name", "history", country);

        when(repositoryMediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(anyInt())).thenReturn(country);
        when(updateEntityUseCase.execute(Publisher.class, publisher, 1)).thenReturn(publisher);

        var outputEntity = updatePublisherUseCase.execute(publisher, 1);
        assertThat(outputEntity).isEqualTo(publisher);
    }

    @Test
    public void should_not_update_a_publisher_whether_country_does_not_exists() throws NoSuchMethodException {

        var country = new Country(1, "name", "gentilic");
        var publisher = new Publisher(1, "name", "history", country);

        when(repositoryMediator.getQuery(Country.class)).thenReturn(countryQuery);
        when(countryQuery.getById(anyInt())).thenReturn(null);

        assertThrows(ValidationException.class, () -> updatePublisherUseCase.execute(publisher, 1));
    }
}
