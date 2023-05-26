package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.GetEntityByIdUseCase;
import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetEntityByIdUseCaseImpl implements GetEntityByIdUseCase {

    private RepositoryMediator mediator;

    public GetEntityByIdUseCaseImpl(@Autowired RepositoryMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(Class<T> clazz, int id) throws NotFoundException, NoSuchMethodException {

        var query = (DataQuery<T>) mediator.getQuery(clazz);

        var domain = query.getById(id);

        if (domain == null) {
            throw new NotFoundException(clazz, id);
        }

        return domain;
    }
}
