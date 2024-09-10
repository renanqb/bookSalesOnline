package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.PublisherData;
import com.renan.booksalesonline.adapters.repositories.mappers.CountryEntityMapper;
import com.renan.booksalesonline.adapters.repositories.mappers.PublisherEntityMapper;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.publisher.PublisherDataQuery;
import com.renan.booksalesonline.domain.Publisher;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class PublisherRepository implements PublisherDataQuery, DataCommand<Publisher> {

    private final PublisherData publisherData;

    @Override
    public List<Publisher> getAll(int page, int size) {

        var pageRequest = PageRequest.of(page, size);
        var publisherEntities = publisherData.findAll(pageRequest);
        return publisherEntities.map(PublisherEntityMapper::toDomain).toList();
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

        return publisherData.existsPublisherByCountryId(countryId);
    }

    @Override
    public List<Publisher> getPublishersByCountryId(int countryId) {

        var publisherEntities = publisherData.getPublishersByCountryId(countryId);
        return PublisherEntityMapper.toDomain(publisherEntities);
    }
}
