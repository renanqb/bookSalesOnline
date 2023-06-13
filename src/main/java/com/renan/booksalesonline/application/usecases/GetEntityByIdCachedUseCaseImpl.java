package com.renan.booksalesonline.application.usecases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.GetEntityByIdUseCase;
import com.renan.booksalesonline.application.ports.out.base.RedisCache;
import com.renan.booksalesonline.domain.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

public class GetEntityByIdCachedUseCaseImpl
        extends GetEntityByIdUseCaseImpl implements GetEntityByIdUseCase {

    private final RedisCache cache;

    public GetEntityByIdCachedUseCaseImpl(
            @Autowired RepositoryMediator mediator,
            @Autowired RedisCache cache
    ) {
        super(mediator);
        this.cache = cache;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(Class<T> clazz, int id)
            throws NotFoundException, NoSuchMethodException, JsonProcessingException {

        var cachedDomain = cache.getById(clazz, id);
        if (cachedDomain != null) return cachedDomain;

        var domain = super.execute(clazz, id);

        cache.save(domain, id);

        return domain;
    }
}
