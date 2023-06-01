package com.renan.booksalesonline.application.usecases.country;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.RemoveEntityUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.country.RemoveCountryUseCase;
import com.renan.booksalesonline.application.ports.out.publisher.PublisherDataQuery;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;
import com.renan.booksalesonline.domain.exceptions.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class RemoveCountryUseCaseImpl implements RemoveCountryUseCase {

    private final RepositoryMediator repositoryMediator;
    private final RemoveEntityUseCase removeEntityUseCase;

    public RemoveCountryUseCaseImpl(
            RepositoryMediator repositoryMediator,
            RemoveEntityUseCase removeEntityUseCase
    ) {
        this.repositoryMediator = repositoryMediator;
        this.removeEntityUseCase = removeEntityUseCase;
    }

    @Override
    public void execute(int id) throws NoSuchMethodException {

        var query = (PublisherDataQuery) repositoryMediator.getQuery(Publisher.class);
        var anyPublisher = query.existsPublisherByCountryId(id);

        if (anyPublisher) {
            throw new ValidationException(Publisher.class);
        }

        removeEntityUseCase.execute(Country.class, id);
    }
}
