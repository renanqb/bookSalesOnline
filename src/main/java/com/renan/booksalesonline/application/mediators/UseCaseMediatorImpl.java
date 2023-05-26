package com.renan.booksalesonline.application.mediators;

import com.renan.booksalesonline.application.ports.in.*;
import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UseCaseMediatorImpl implements UseCaseMediator {

    private HashMap<Class, Object> useCases = new HashMap<>();

    public UseCaseMediatorImpl(
            @Autowired GetAllEntitiesUseCase getAllEntitiesUseCase,
            @Autowired GetEntityByIdUseCase getEntityByIdUseCase,
            @Autowired CreateEntityUseCase createEntityUseCase,
            @Autowired UpdateEntityUseCase updateEntityUseCase,
            @Autowired RemoveEntityUseCase removeEntityUseCase
    ) {
        useCases.put(GetAllEntitiesUseCase.class, getAllEntitiesUseCase);
        useCases.put(GetEntityByIdUseCase.class, getEntityByIdUseCase);
        useCases.put(CreateEntityUseCase.class, createEntityUseCase);
        useCases.put(UpdateEntityUseCase.class, updateEntityUseCase);
        useCases.put(RemoveEntityUseCase.class, removeEntityUseCase);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) throws NoSuchMethodException {

        var useCase = (T) useCases.get(clazz);

        if (useCase == null) {
            throw new NoSuchMethodException("There is no use case provider");
        }

        return useCase;
    }
}
