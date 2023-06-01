package com.renan.booksalesonline.application.usecases.publisher;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.publisher.PublisherDataQuery;
import com.renan.booksalesonline.domain.Publisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetPublishersByCountryUseCaseImplTest {

    @Mock
    private PublisherDataQuery publisherDataQuery;
    @Mock private RepositoryMediator repositoryMediator;
    @InjectMocks private GetPublishersByCountryUseCaseImpl getPublishersByCountryUseCase;

    @Test
    public void should_get_publishers_given_a_country() throws NoSuchMethodException {

        var expectedPublishers = Arrays.asList(
                new Publisher(1, "name1", "history1", null),
                new Publisher(2, "name2", "history2", null),
                new Publisher(3, "name3", "history3", null)
        );

        when(repositoryMediator.getQuery(Publisher.class)).thenReturn(publisherDataQuery);
        when(publisherDataQuery.getPublishersByCountryId(anyInt())).thenReturn(expectedPublishers);

        assertThat(getPublishersByCountryUseCase.execute(1)).isEqualTo(expectedPublishers);
    }
}
