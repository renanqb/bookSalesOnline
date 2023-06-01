package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.PublicationEntity;
import com.renan.booksalesonline.adapters.repositories.entities.PublisherEntity;
import com.renan.booksalesonline.domain.Publication;
import com.renan.booksalesonline.domain.Publisher;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PublicationEntityMapperTest {

    @Test
    public void should_return_null_when_publication_entity_is_null() {

        assertThat(PublicationEntityMapper.toDomain((PublicationEntity) null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_publication_entity_to_publication_domain() {

        var publisherEntity = new PublisherEntity(1, "name", "history", null);
        var publicationEntity = new PublicationEntity(1, "name", publisherEntity);

        var publisher = new Publisher(1, "name", "history", null);
        var expectedPublication = new Publication(1, "name", publisher);
        var actualPublication = PublicationEntityMapper.toDomain(publicationEntity);

        assertThat(actualPublication)
                .usingRecursiveComparison()
                .isEqualTo(expectedPublication);
    }

    @Test
    public void should_return_null_when_publication_domain_is_null() {

        assertThat(PublicationEntityMapper.fromDomain(null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_publication_domain_to_publication_entity() {

        var publisher = new Publisher(1, "name", "history", null);
        var publication = new Publication(1, "name", publisher);
        var publisherEntity = new PublisherEntity(1, "name", "history", null);
        var expectedPublicationEntity = new PublicationEntity(1, "name", publisherEntity);
        var actualPublicationEntity = PublicationEntityMapper.fromDomain(publication);

        assertThat(actualPublicationEntity)
                .usingRecursiveComparison()
                .isEqualTo(expectedPublicationEntity);
    }

    @Test
    public void should_return_publication_empty_list_when_domain_list_is_null() {

        assertThat(PublicationEntityMapper.toDomain((List<PublicationEntity>) null))
                .usingRecursiveComparison()
                .isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void should_parse_publisher_entities_to_publisher_domains() {

        var publisherEntity = new PublisherEntity(1, "name", "history", null);
        var entities = Arrays.asList(
                new PublicationEntity(1, "name1", publisherEntity),
                new PublicationEntity(2, "name2", publisherEntity),
                new PublicationEntity(3, "name3", publisherEntity)
        );

        var publisher = new Publisher(1, "name", "history", null);
        var expectedPublications = Arrays.asList(
                new Publication(1, "name1", publisher),
                new Publication(2, "name2", publisher),
                new Publication(3, "name3", publisher)
        );
        var actualPublications = PublicationEntityMapper.toDomain(entities);

        assertThat(actualPublications)
                .usingRecursiveComparison()
                .isEqualTo(expectedPublications);
    }
}
