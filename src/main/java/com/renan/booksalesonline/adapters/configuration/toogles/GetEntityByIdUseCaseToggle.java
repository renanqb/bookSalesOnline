package com.renan.booksalesonline.adapters.configuration.toogles;

import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.application.ports.in.usecases.GetEntityByIdUseCase;
import com.renan.booksalesonline.application.usecases.GetEntityByIdUseCaseImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetEntityByIdUseCaseToggle {

    @Bean
    @ConditionalOnProperty(name = "features.cache.experimental", havingValue = "false")
    public GetEntityByIdUseCase getEntityByIdFromDatabase(
            @Autowired RepositoryMediator mediator) {

        System.out.println("features.cache.experimental=false");

        return new GetEntityByIdUseCaseImpl(mediator);
    }
}
