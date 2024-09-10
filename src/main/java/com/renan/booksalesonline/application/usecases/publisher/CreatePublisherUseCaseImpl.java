package com.renan.booksalesonline.application.usecases.publisher;

import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.CreateEntityUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.CreatePublisherUseCase;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;
import com.renan.booksalesonline.domain.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreatePublisherUseCaseImpl implements CreatePublisherUseCase {

    private final RepositoryMediator repositoryMediator;
    private final CreateEntityUseCase createEntityUseCaseImpl;

    public Publisher execute(@NotNull Publisher publisher) throws NoSuchMethodException {

        var query = repositoryMediator.getQuery(Country.class);
        var country = query.getById(publisher.getCountry().getId());

        if (country == null) {
            throw new ValidationException(Publisher.class);
        }

        return createEntityUseCaseImpl.execute(Publisher.class, publisher);
    }
}
