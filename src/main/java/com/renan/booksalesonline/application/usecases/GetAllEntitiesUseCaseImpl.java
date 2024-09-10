package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.usecases.GetAllEntitiesUseCase;
import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetAllEntitiesUseCaseImpl implements GetAllEntitiesUseCase {

    private final RepositoryMediator mediator;

    @Override
    public <T> List<T> execute(Class<T> clazz) throws NoSuchMethodException {

        return mediator.getQuery(clazz).getAll(0, 20);
    }
}
