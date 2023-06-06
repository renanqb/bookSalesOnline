package com.renan.booksalesonline.application.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.GetEntityByIdUseCase;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.application.ports.out.base.RedisCache;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetEntityByIdUseCaseImpl implements GetEntityByIdUseCase {

    private final RepositoryMediator mediator;

    private final RedisCache cache;

    public GetEntityByIdUseCaseImpl(
            @Autowired RepositoryMediator mediator,
            @Autowired RedisCache cache
    ) {
        this.mediator = mediator;
        this.cache = cache;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(Class<T> clazz, int id)
            throws NotFoundException, NoSuchMethodException, JsonProcessingException {

        var cachedDomain = cache.getById(clazz, id);
        if (cachedDomain != null) return cachedDomain;

        var query = (DataQuery<T>) mediator.getQuery(clazz);
        var domain = query.getById(id);

        if (domain == null) {
            throw new NotFoundException(clazz, id);
        }

        cache.save(domain, id);

        return domain;
    }
}
