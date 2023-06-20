package com.renan.booksalesonline.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.model.SubjectDto;
import com.renan.booksalesonline.domain.Subject;

import java.util.List;

public class SubjectDtoMapper {

    public static Subject toDomain(SubjectDto subjectRequest) {

        return new Subject(
                (int)subjectRequest.getId(),
                subjectRequest.getName(),
                subjectRequest.getDescription()
        );
    }

    public static SubjectDto fromDomain(Subject subject) {

        var subjectDto = new SubjectDto(subject.getName(), subject.getDescription());
        subjectDto.setId(subject.getId());

        return subjectDto;
    }

    public static SubjectDto[] fromDomain(List<Subject> subjects){

        return subjects.stream()
                .map(SubjectDtoMapper::fromDomain)
                .toArray(SubjectDto[]::new);
    }
}
