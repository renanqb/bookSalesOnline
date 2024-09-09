package com.renan.booksalesonline.application.usecases;

import com.renan.booksalesonline.application.ports.in.usecases.CreateEntityUseCase;
import com.renan.booksalesonline.application.ports.in.common.RepositoryMediator;
import com.renan.booksalesonline.domain.commom.BaseDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateEntityUseCaseImpl implements CreateEntityUseCase {

    protected final RepositoryMediator mediator;

    @Override
    public <T> T execute(Class<T> clazz, BaseDomain domain) throws NoSuchMethodException {

        var command = mediator.getCommand(clazz);
        return command.save((T)domain);
    }
}
