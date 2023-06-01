package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.PublisherData;
import com.renan.booksalesonline.adapters.repositories.mappers.PublisherEntityMapper;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.publisher.PublisherDataQuery;
import com.renan.booksalesonline.domain.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublisherRepository implements PublisherDataQuery, DataCommand<Publisher> {

    private PublisherData publisherData;

    public PublisherRepository(@Autowired PublisherData publisherData) {
        this.publisherData = publisherData;
    }

    @Override
    public List<Publisher> getAll() {

        var publisherEntities = publisherData.findAll();
        return PublisherEntityMapper.toDomain(publisherEntities);
    }

    @Override
    public Publisher getById(int id) {

        var optPublisherEntity = publisherData.findById(id);
        return optPublisherEntity
                .map(PublisherEntityMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Publisher save(Publisher publisher) {

        var publisherEntity = PublisherEntityMapper.fromDomain(publisher);
        publisherData.save(publisherEntity);
        publisher.setId(publisherEntity.getId());

        return publisher;
    }

    @Override
    public void remove(Publisher publisher) {

        var publisherEntity = PublisherEntityMapper.fromDomain(publisher);
        publisherData.delete(publisherEntity);
    }

    @Override
    public boolean existsPublisherByCountryId(int countryId) {

        var exists = publisherData.existsPublisherByCountryId(countryId);
        return exists;
    }

    @Override
    public List<Publisher> getPublishersByCountryId(int countryId) {

        var publisherEntities = publisherData.getPublishersByCountryId(countryId);
        return PublisherEntityMapper.toDomain(publisherEntities);
    }
}
