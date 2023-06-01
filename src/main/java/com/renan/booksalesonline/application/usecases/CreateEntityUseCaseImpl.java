package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.usecases.CreateEntityUseCase;
import com.renan.booksalesonline.application.ports.in.commom.RepositoryMediator;
import com.renan.booksalesonline.domain.commom.BaseDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateEntityUseCaseImpl implements CreateEntityUseCase {

    protected final RepositoryMediator mediator;

    public CreateEntityUseCaseImpl(@Autowired RepositoryMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(Class<T> clazz, BaseDomain domain) throws NoSuchMethodException {

        var command = mediator.getCommand(clazz);
        return command.save((T)domain);
    }
}
