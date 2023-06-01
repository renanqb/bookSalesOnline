package com.renan.booksalesonline.application.ports.in.usecases.country;

import com.renan.booksalesonline.domain.Publisher;

import java.util.List;

public interface GetPublishersByCountryUseCase {

    List<Publisher> execute(int countryId) throws NoSuchMethodException;
}
