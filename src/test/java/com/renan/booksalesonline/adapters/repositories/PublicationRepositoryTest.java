package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.PublicationData;
import com.renan.booksalesonline.adapters.repositories.entities.PublicationEntity;
import com.renan.booksalesonline.adapters.repositories.entities.PublisherEntity;
import com.renan.booksalesonline.domain.Publication;
import com.renan.booksalesonline.domain.Publisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublicationRepositoryTest {

    @Mock private PublicationData publicationData;
    @InjectMocks private PublicationRepository publicationRepository;

    @Test
    public void should_get_all_publications_entities() {

        var publisherEntity = new PublisherEntity(1, "publisher", "history", null);
        var publicationEntities = Arrays.asList(
                new PublicationEntity(1, "name1", publisherEntity),
                new PublicationEntity(2, "name2", publisherEntity),
                new PublicationEntity(3, "name3", publisherEntity)
        );
        when(publicationData.findAll()).thenReturn(publicationEntities);

        var publisher = new Publisher(1, "publisher", "history", null);
        var expectedPublications = Arrays.asList(
                new Publication(1, "name1", publisher),
                new Publication(2, "name2", publisher),
                new Publication(3, "name3", publisher)
        );

        var publications = publicationRepository.getAll();

        for (var i = 0; i < 3; i++) {
            var expected = expectedPublications.get(i);
            var actual = publications.get(i);

            assertThat((int)actual.getId()).isEqualTo((int)expected.getId());
            assertThat(actual.getName()).isEqualTo(expected.getName());
            assertThat(actual.getPublisher()).isNotNull();
        }
    }

    @Test
    public void should_get_by_id_one_publication_entity() {

        var publicationEntity = new PublicationEntity(1, "publication", null);
        when(publicationData.findById(anyInt())).thenReturn(Optional.of(publicationEntity));

        var expectedPublication = new Publication(1, "publication", null);
        var actualPublication = publicationRepository.getById(1);

        assertThat((int)actualPublication.getId()).isEqualTo((int)expectedPublication.getId());
        assertThat(actualPublication.getName()).isEqualTo(expectedPublication.getName());
    }

    @Test
    public void should_validate_whether_exists_publication_by_publisher() {

        when(publicationData.existsPublicationByPublisherId (anyInt())).thenReturn(true);
        assertThat(publicationRepository.existsPublicationByPublisherId(1)).isTrue();
    }
}
