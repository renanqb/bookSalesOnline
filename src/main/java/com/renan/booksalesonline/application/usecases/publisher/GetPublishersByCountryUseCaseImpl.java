package com.renan.booksalesonline.application.usecases.publisher;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.country.GetPublishersByCountryUseCase;
import com.renan.booksalesonline.application.ports.out.publisher.PublisherDataQuery;
import com.renan.booksalesonline.domain.Publisher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetPublishersByCountryUseCaseImpl implements GetPublishersByCountryUseCase {

    private final RepositoryMediator repositoryMediator;

    @Override
    public List<Publisher> execute(int countryId) throws NoSuchMethodException {

        var query = (PublisherDataQuery) repositoryMediator.getQuery(Publisher.class);
        return query.getPublishersByCountryId(countryId);
    }
}
