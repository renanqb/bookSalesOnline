package com.renan.booksalesonline.application.ports.out.publisher;

import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.Publisher;

import java.util.List;

public interface PublisherDataQuery extends DataQuery<Publisher> {

    boolean existsPublisherByCountryId(int countryId);
    List<Publisher> getPublishersByCountryId(int countryId);
}
