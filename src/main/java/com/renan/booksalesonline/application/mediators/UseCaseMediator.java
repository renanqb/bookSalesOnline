package com.renan.booksalesonline.application.mediators;

import com.renan.booksalesonline.application.ports.in.country.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UseCaseMediator {

    private HashMap<UseCaseType, Object> useCases = new HashMap<>();;

    public UseCaseMediator(
            @Autowired GetAllCountriesUseCase getAllCountriesUseCase,
            @Autowired GetCountryByIdUseCase getCountryByIdUseCase,
            @Autowired CreateCountryUseCase createCountryUseCase,
            @Autowired UpdateCountryUseCase updateCountryUseCase,
            @Autowired RemoveCountryUseCase removeCountryUseCase) {

        useCases.put(UseCaseType.COUNTRY_CREATE, createCountryUseCase);
        useCases.put(UseCaseType.COUNTRY_UPDATE, updateCountryUseCase);
        useCases.put(UseCaseType.COUNTRY_REMOVE, removeCountryUseCase);
        useCases.put(UseCaseType.COUNTRY_GET_ALL, getAllCountriesUseCase);
        useCases.put(UseCaseType.COUNTRY_GET_BY_ID, getCountryByIdUseCase);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(UseCaseType useCaseType) throws NoSuchMethodException {

        var useCase = (T) useCases.get(useCaseType);

        if (useCase == null)
            throw new NoSuchMethodException("There is no use case provider");

        return useCase;
    }
}
