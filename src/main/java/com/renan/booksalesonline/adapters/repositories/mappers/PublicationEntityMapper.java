package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.PublicationEntity;
import com.renan.booksalesonline.domain.Publication;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PublicationEntityMapper {

    public static Publication toDomain(PublicationEntity publicationEntity) {

        if (publicationEntity == null)
            return null;

        return new Publication(
                publicationEntity.getId(),
                publicationEntity.getName(),
                PublisherEntityMapper.toDomain(publicationEntity.getPublisher())
        );
    }

    public static List<Publication> toDomain(List<PublicationEntity> publisherEntities) {

        if (publisherEntities == null)
            return Collections.emptyList();

        return publisherEntities.stream()
                .map(PublicationEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static PublicationEntity fromDomain(Publication publication) {

        if (publication == null)
            return null;

        return new PublicationEntity(
                (Integer) publication.getId(),
                publication.getName(),
                PublisherEntityMapper.fromDomain(publication.getPublisher())
        );
    }
}
