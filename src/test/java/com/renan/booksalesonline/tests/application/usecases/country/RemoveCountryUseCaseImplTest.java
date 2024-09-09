package com.renan.booksalesonline.tests.application.usecases.country;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.RemoveEntityUseCase;
import com.renan.booksalesonline.application.ports.out.publisher.PublisherDataQuery;
import com.renan.booksalesonline.application.usecases.country.RemoveCountryUseCaseImpl;
import com.renan.booksalesonline.domain.Publisher;
import com.renan.booksalesonline.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RemoveCountryUseCaseImplTest {

    @Mock private PublisherDataQuery publisherDataQuery;
    @Mock private RepositoryMediator repositoryMediator;
    @Mock private RemoveEntityUseCase removeEntityUseCase;
    @InjectMocks private RemoveCountryUseCaseImpl removeCountryUseCase;

    @Test
    public void should_remove_a_country_whether_it_is_not_being_referenced() throws NoSuchMethodException {

        when(repositoryMediator.getQuery(Publisher.class)).thenReturn(publisherDataQuery);
        when(publisherDataQuery.existsPublisherByCountryId(anyInt())).thenReturn(false);
        doNothing().when(removeEntityUseCase).execute(any(), anyInt());

        assertDoesNotThrow(() -> {
            removeCountryUseCase.execute(1);
        });
    }

    @Test
    public void should_not_remove_a_country_whether_it_is_being_referenced() throws NoSuchMethodException {

        when(repositoryMediator.getQuery(Publisher.class)).thenReturn(publisherDataQuery);
        when(publisherDataQuery.existsPublisherByCountryId(anyInt())).thenReturn(true);

        assertThrows(ValidationException.class, () -> removeCountryUseCase.execute(1));
    }
}
