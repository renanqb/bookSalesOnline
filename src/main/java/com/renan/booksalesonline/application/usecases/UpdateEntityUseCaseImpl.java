package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.UpdateEntityUseCase;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.commom.BaseDomain;
import com.renan.booksalesonline.domain.exceptions.UpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateEntityUseCaseImpl implements UpdateEntityUseCase {

    private final RepositoryMediator mediator;

    public UpdateEntityUseCaseImpl(@Autowired RepositoryMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(Class<T> clazz, BaseDomain domain, int id) throws NoSuchMethodException, UpdateException {

        var query = (DataQuery<T>) mediator.getQuery(clazz);
        var command = (DataCommand<T>) mediator.getCommand(clazz);

        var persisted = query.getById(id);

        if (persisted == null) {
            throw new UpdateException(clazz, id);
        }

        domain.setId(id);

        return command.save((T) domain);
    }
}
