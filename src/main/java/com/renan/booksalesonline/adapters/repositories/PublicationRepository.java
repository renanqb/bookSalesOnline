package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.PublicationData;
import com.renan.booksalesonline.adapters.repositories.mappers.PublicationEntityMapper;
import com.renan.booksalesonline.application.ports.out.publication.PublicationDataQuery;
import com.renan.booksalesonline.domain.Publication;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class PublicationRepository implements PublicationDataQuery {

    private final PublicationData publicationData;

    public List<Publication> getAll() {

        var publicationEntities = publicationData.findAll();
        return PublicationEntityMapper.toDomain(publicationEntities);
    }

    @Override
    public Publication getById(int id) {

        var optPublicationEntity = publicationData.findById(id);
        return optPublicationEntity
                .map(PublicationEntityMapper::toDomain)
                .orElse(null);
    }

    @Override
    public List<Publication> getAll(int page, int size) {

        var pageable = PageRequest.of(page, size);
        var publicationEntities = publicationData.findAll(pageable);
        return publicationEntities.map(PublicationEntityMapper::toDomain).toList();
    }

    @Override
    public boolean existsPublicationByPublisherId(int publisherId) {

        return publicationData.existsPublicationByPublisherId(publisherId);
    }
}
