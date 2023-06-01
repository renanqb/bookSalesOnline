package com.renan.booksalesonline.application.usecases.publisher;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.country.GetPublishersByCountryUseCase;
import com.renan.booksalesonline.application.ports.out.publisher.PublisherDataQuery;
import com.renan.booksalesonline.domain.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPublishersByCountryUseCaseImpl implements GetPublishersByCountryUseCase {

    private final RepositoryMediator repositoryMediator;

    public GetPublishersByCountryUseCaseImpl(
            @Autowired RepositoryMediator repositoryMediator
    ) {
        this.repositoryMediator = repositoryMediator;
    }

    @Override
    public List<Publisher> execute(int countryId) throws NoSuchMethodException {

        var query = (PublisherDataQuery) repositoryMediator.getQuery(Publisher.class);
        return query.getPublishersByCountryId(countryId);
    }
}
