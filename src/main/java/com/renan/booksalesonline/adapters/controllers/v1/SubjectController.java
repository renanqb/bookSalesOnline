package com.renan.booksalesonline.adapters.controllers.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.renan.booksalesonline.adapters.controllers.v1.mappers.SubjectDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.SubjectDto;
import com.renan.booksalesonline.application.ports.in.common.UseCaseMediator;
import com.renan.booksalesonline.application.ports.in.usecases.*;
import com.renan.booksalesonline.domain.Subject;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class SubjectController {

    private final UseCaseMediator mediator;

    @GetMapping("/subjects")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectDto[] getAllSubjects() throws NoSuchMethodException {

        var subjects = mediator
                .get(GetAllEntitiesUseCase.class)
                .execute(Subject.class);

        return SubjectDtoMapper.fromDomain(subjects);
    }

    @GetMapping("/subjects/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectDto getSubjectById(@PathVariable("id") int id)
            throws NoSuchMethodException, JsonProcessingException {

        var subject = mediator
                .get(GetEntityByIdUseCase.class)
                .execute(Subject.class, id);

        return SubjectDtoMapper.fromDomain(subject);
    }

    @PostMapping("/subjects")
    @ResponseStatus(value = HttpStatus.CREATED)
    public SubjectDto create(@RequestBody SubjectDto subjectRequest) throws NoSuchMethodException {

        var subject = SubjectDtoMapper.toDomain(subjectRequest);
        var createdSubject = mediator
                .get(CreateEntityUseCase.class)
                .execute(Subject.class, subject);

        return SubjectDtoMapper.fromDomain(createdSubject);
    }

    @PutMapping("/subjects/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public SubjectDto update(@PathVariable int id, @RequestBody SubjectDto subjectRequest)
            throws NoSuchMethodException {

        var subject = SubjectDtoMapper.toDomain(subjectRequest);
        var updatedSubject = mediator
                .get(UpdateEntityUseCase.class)
                .execute(Subject.class, subject, id);

        return SubjectDtoMapper.fromDomain(updatedSubject);
    }

    @DeleteMapping("/subjects/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) throws NoSuchMethodException {

        var useCase = mediator.get(RemoveEntityUseCase.class);
        useCase.execute(Subject.class, id);
    }
}

