package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.PublicationData;
import com.renan.booksalesonline.adapters.repositories.mappers.PublicationEntityMapper;
import com.renan.booksalesonline.adapters.repositories.mappers.PublisherEntityMapper;
import com.renan.booksalesonline.application.ports.out.publication.PublicationDataQuery;
import com.renan.booksalesonline.domain.Publication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublicationRepository implements PublicationDataQuery {

    private final PublicationData publicationData;

    public PublicationRepository(@Autowired PublicationData publicationData) {
        this.publicationData = publicationData;
    }

    @Override
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
    public boolean existsPublicationByPublisherId(int publisherId) {
        return publicationData.existsPublicationByPublisherId(publisherId);
    }
}
