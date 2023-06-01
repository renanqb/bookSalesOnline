package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.usecases.GetAllEntitiesUseCase;
import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllEntitiesUseCaseImpl implements GetAllEntitiesUseCase {

    private final RepositoryMediator mediator;

    public GetAllEntitiesUseCaseImpl(@Autowired RepositoryMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> execute(Class<T> clazz) throws NoSuchMethodException {

        return mediator.getQuery(clazz).getAll();
    }
}
