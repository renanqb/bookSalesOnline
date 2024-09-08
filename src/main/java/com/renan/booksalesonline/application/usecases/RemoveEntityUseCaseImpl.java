package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.usecases.RemoveEntityUseCase;
import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.domain.exceptions.RemoveException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RemoveEntityUseCaseImpl implements RemoveEntityUseCase {

    private final RepositoryMediator mediator;

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
