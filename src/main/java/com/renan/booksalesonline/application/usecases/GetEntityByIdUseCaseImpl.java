package com.renan.booksalesonline.application.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.GetEntityByIdUseCase;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetEntityByIdUseCaseImpl implements GetEntityByIdUseCase {

    protected final RepositoryMediator mediator;

    @Override
    public <T> T execute(Class<T> clazz, int id)
            throws NotFoundException, NoSuchMethodException, JsonProcessingException {

        var query = mediator.getQuery(clazz);
        var domain = query.getById(id);

        if (domain == null) {
            throw new NotFoundException(clazz, id);
        }

        return domain;
    }
}
