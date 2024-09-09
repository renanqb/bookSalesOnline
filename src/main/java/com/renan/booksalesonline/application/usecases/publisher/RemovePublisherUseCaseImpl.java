package com.renan.booksalesonline.application.usecases.publisher;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.RemoveEntityUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.RemovePublisherUseCase;
import com.renan.booksalesonline.application.ports.out.publication.PublicationDataQuery;
import com.renan.booksalesonline.domain.Publication;
import com.renan.booksalesonline.domain.Publisher;
import com.renan.booksalesonline.domain.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RemovePublisherUseCaseImpl implements RemovePublisherUseCase {

    private final RepositoryMediator repositoryMediator;
    private final RemoveEntityUseCase removeEntityUseCase;

    @Override
    public void execute(int id) throws NoSuchMethodException {

        var query = (PublicationDataQuery) repositoryMediator.getQuery(Publication.class);
        var anyPublication = query.existsPublicationByPublisherId(id);

        if (anyPublication) {
            throw new ValidationException(Publisher.class);
        }

        removeEntityUseCase.execute(Publisher.class, id);
    }
}
