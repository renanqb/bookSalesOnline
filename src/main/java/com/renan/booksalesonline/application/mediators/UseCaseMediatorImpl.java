package com.renan.booksalesonline.application.mediators;

import com.renan.booksalesonline.application.ports.in.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.country.*;
import com.renan.booksalesonline.application.ports.in.publisher.GetAllPublishersUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UseCaseMediatorImpl implements UseCaseMediator {

    private HashMap<Class, Object> useCases = new HashMap<>();;

    public UseCaseMediatorImpl(
            @Autowired GetAllCountriesUseCase getAllCountriesUseCase,
            @Autowired GetCountryByIdUseCase getCountryByIdUseCase,
            @Autowired CreateCountryUseCase createCountryUseCase,
            @Autowired UpdateCountryUseCase updateCountryUseCase,
            @Autowired RemoveCountryUseCase removeCountryUseCase,
            @Autowired GetAllPublishersUseCase getAllPublishersUseCase) {

        // COUNTRY
        useCases.put(CreateCountryUseCase.class, createCountryUseCase);
        useCases.put(UpdateCountryUseCase.class, updateCountryUseCase);
        useCases.put(RemoveCountryUseCase.class, removeCountryUseCase);
        useCases.put(GetAllCountriesUseCase.class, getAllCountriesUseCase);
        useCases.put(GetCountryByIdUseCase.class, getCountryByIdUseCase);
        // PUBLISHER
        useCases.put(GetAllPublishersUseCase.class, getAllPublishersUseCase);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) throws NoSuchMethodException {

        var useCase = (T) useCases.get(clazz);

        if (useCase == null)
            throw new NoSuchMethodException("There is no use case provider");

        return useCase;
    }
}
