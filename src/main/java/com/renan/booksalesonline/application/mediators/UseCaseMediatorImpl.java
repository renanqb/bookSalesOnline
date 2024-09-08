package com.renan.booksalesonline.application.mediators;

import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.usecases.*;
import com.renan.booksalesonline.application.ports.in.usecases.country.GetPublishersByCountryUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.country.RemoveCountryUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.GetAllPublicationImagesUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.CreatePublicationImageUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publication.UpdatePublicationImageUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.CreatePublisherUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.RemovePublisherUseCase;
import com.renan.booksalesonline.application.ports.in.usecases.publisher.UpdatePublisherUseCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UseCaseMediatorImpl implements UseCaseMediator {

    private final HashMap<Class, Object> useCases = new HashMap<>();

    public UseCaseMediatorImpl(
            @Autowired GetAllEntitiesUseCase getAllEntitiesUseCase,
            @Autowired GetEntityByIdUseCase getEntityByIdUseCase,
            @Autowired CreateEntityUseCase createEntityUseCase,
            @Autowired UpdateEntityUseCase updateEntityUseCase,
            @Autowired RemoveEntityUseCase removeEntityUseCase,

            @Autowired RemoveCountryUseCase removeCountryUseCase,
            @Autowired CreatePublisherUseCase createPublisherUseCase,
            @Autowired UpdatePublisherUseCase updatePublisherUseCase,
            @Autowired RemovePublisherUseCase removePublisherUseCase,
            @Autowired GetPublishersByCountryUseCase getPublishersByCountryUseCase,
            @Autowired GetAllPublicationImagesUseCase getAllPublicationImagesUseCase,
            @Autowired CreatePublicationImageUseCase createPublicationImageUseCase,
            @Autowired UpdatePublicationImageUseCase updatePublicationImageUseCase
    ) {
        useCases.put(GetAllEntitiesUseCase.class, getAllEntitiesUseCase);
        useCases.put(GetEntityByIdUseCase.class, getEntityByIdUseCase);
        useCases.put(CreateEntityUseCase.class, createEntityUseCase);
        useCases.put(UpdateEntityUseCase.class, updateEntityUseCase);
        useCases.put(RemoveEntityUseCase.class, removeEntityUseCase);

        useCases.put(RemoveCountryUseCase.class, removeCountryUseCase);
        useCases.put(CreatePublisherUseCase.class, createPublisherUseCase);
        useCases.put(UpdatePublisherUseCase.class, updatePublisherUseCase);
        useCases.put(RemovePublisherUseCase.class, removePublisherUseCase);
        useCases.put(GetPublishersByCountryUseCase.class, getPublishersByCountryUseCase);
        useCases.put(GetAllPublicationImagesUseCase.class, getAllPublicationImagesUseCase);
        useCases.put(CreatePublicationImageUseCase.class, createPublicationImageUseCase);
        useCases.put(UpdatePublicationImageUseCase.class, updatePublicationImageUseCase);
    }

    @Override
    public <T> T get(Class<T> clazz) throws NoSuchMethodException {

        var useCase = (T) useCases.get(clazz);
        if (useCase == null) {
            throw new NoSuchMethodException("There is no use case provider");
        }
        return useCase;
    }
}
