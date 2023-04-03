package com.renan.booksalesonline.application.mediators;

import com.renan.booksalesonline.application.ports.in.GetAllCountriesUseCase;
import com.renan.booksalesonline.application.ports.in.GetCountryByIdUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UseCaseMediator {

    private GetAllCountriesUseCase getAllCountriesUseCase;
    private GetCountryByIdUseCase getCountryByIdUseCase;

    public UseCaseMediator(
            @Autowired GetAllCountriesUseCase getAllCountriesUseCase,
            @Autowired GetCountryByIdUseCase getCountryByIdUseCase) {

        this.getAllCountriesUseCase = getAllCountriesUseCase;
        this.getCountryByIdUseCase = getCountryByIdUseCase;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(UseCaseType useCaseType) throws NoSuchMethodException {
        switch (useCaseType) {
            case COUNTRY_GET_ALL:
                return (T) getAllCountriesUseCase;
            case COUNTRY_GET_BY_ID:
                return (T) getCountryByIdUseCase;
            default:
                throw new NoSuchMethodException("There is no use case provider");
        }
    }
}
