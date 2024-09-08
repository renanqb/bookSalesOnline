package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.UpdateEntityUseCase;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.commom.BaseDomain;
import com.renan.booksalesonline.domain.exceptions.UpdateException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateEntityUseCaseImpl implements UpdateEntityUseCase {

    private final RepositoryMediator mediator;

    @Override
    public <T> T execute(Class<T> clazz, BaseDomain domain, int id)
            throws NoSuchMethodException, UpdateException {

        var query = mediator.getQuery(clazz);
        var command = mediator.getCommand(clazz);

        var persisted = query.getById(id);

        if (persisted == null) {
            throw new UpdateException(clazz, id);
        }

        domain.setId(id);

        return command.save((T) domain);
    }
}
