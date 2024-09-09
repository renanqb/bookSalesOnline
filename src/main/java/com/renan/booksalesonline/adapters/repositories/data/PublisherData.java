package com.renan.booksalesonline.adapters.repositories.data;

import com.renan.booksalesonline.adapters.repositories.entities.PublisherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherData extends JpaRepository<PublisherEntity, Integer> {

    boolean existsPublisherByCountryId(int countryId);
    List<PublisherEntity> getPublishersByCountryId(int countryId);
}
