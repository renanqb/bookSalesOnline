package com.renan.booksalesonline.application.usecases.publisher;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.RemoveEntityUseCase;
import com.renan.booksalesonline.application.ports.out.publication.PublicationDataQuery;
import com.renan.booksalesonline.domain.Publication;
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
public class RemovePublisherUseCaseImplTest {

    @Mock private PublicationDataQuery publicationDataQuery;
    @Mock private RepositoryMediator repositoryMediator;
    @Mock private RemoveEntityUseCase removeEntityUseCase;
    @InjectMocks private RemovePublisherUseCaseImpl removePublisherUseCase;

    @Test
    public void should_remove_a_publisher_whether_country_does_exists() throws NoSuchMethodException {

        when(repositoryMediator.getQuery(Publication.class)).thenReturn(publicationDataQuery);
        when(publicationDataQuery.existsPublicationByPublisherId(anyInt())).thenReturn(false);
        doNothing().when(removeEntityUseCase).execute(any(), anyInt());

        assertDoesNotThrow(() -> {
            removePublisherUseCase.execute(1);
        });
    }

    @Test
    public void should_not_remove_a_publisher_whether_country_does_not_exists() throws NoSuchMethodException {

        when(repositoryMediator.getQuery(Publication.class)).thenReturn(publicationDataQuery);
        when(publicationDataQuery.existsPublicationByPublisherId(anyInt())).thenReturn(true);

        assertThrows(ValidationException.class, () -> removePublisherUseCase.execute(1));
    }
}
