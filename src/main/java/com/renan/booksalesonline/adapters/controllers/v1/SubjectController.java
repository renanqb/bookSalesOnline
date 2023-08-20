package com.renan.booksalesonline.adapters.controllers.v1;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.SubjectDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.SubjectDto;
import com.renan.booksalesonline.application.ports.in.commom.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.usecases.GetAllEntitiesUseCase;
import com.renan.booksalesonline.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubjectController {

    private final UseCaseMediator mediator;

    public SubjectController(@Autowired UseCaseMediator mediator) {
        this.mediator = mediator;
    }

    @GetMapping("/subjects")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectDto[] getAllSubjects() throws NoSuchMethodException {

        var subjects = mediator
                .get(GetAllEntitiesUseCase.class)
                .execute(Subject.class);

        return SubjectDtoMapper.fromDomain(subjects);
    }

}
