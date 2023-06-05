package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.PublisherEntity;
import com.renan.booksalesonline.domain.Publisher;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PublisherEntityMapper {

    public static Publisher toDomain(PublisherEntity publisherEntity) {

        if (publisherEntity == null)
            return null;

        return new Publisher(
                publisherEntity.getId(),
                publisherEntity.getName(),
                publisherEntity.getHistory(),
                CountryEntityMapper.toDomain(publisherEntity.getCountry())
        );
    }

    public static List<Publisher> toDomain(List<PublisherEntity> publisherEntities) {

        if (publisherEntities == null)
            return Collections.emptyList();

        return publisherEntities.stream()
                .map(PublisherEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static PublisherEntity fromDomain(Publisher publisher) {

        if (publisher == null)
            return null;

        return new PublisherEntity(
                publisher.getId(),
                publisher.getName(),
                publisher.getHistory(),
                CountryEntityMapper.fromDomain(publisher.getCountry())
        );
    }
}
