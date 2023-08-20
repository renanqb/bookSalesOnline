package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.usecases.RemoveEntityUseCase;
import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.exceptions.RemoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoveEntityUseCaseImpl implements RemoveEntityUseCase {

    private final RepositoryMediator mediator;

    public RemoveEntityUseCaseImpl(@Autowired RepositoryMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public <T> void execute(Class<T> clazz, int id) throws NoSuchMethodException {

        var query = mediator.getQuery(clazz);
        var command = mediator.getCommand(clazz);

        var persistedCountry = query.getById(id);

        if (persistedCountry == null) {
            throw new RemoveException(clazz, id);
        }

        command.remove(persistedCountry);
    }
}
