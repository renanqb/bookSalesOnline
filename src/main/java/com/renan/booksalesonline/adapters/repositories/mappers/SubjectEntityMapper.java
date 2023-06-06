package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.SubjectEntity;
import com.renan.booksalesonline.domain.Subject;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectEntityMapper {

    public static Subject toDomain(SubjectEntity subjectEntity){

        if (subjectEntity == null)
            return null;
        
        return new Subject(
                subjectEntity.getId(),
                subjectEntity.getName(),
                subjectEntity.getDescription()
        );
    }

    public static List<Subject> toDomain(List<SubjectEntity> subjectEntities) {

        if (subjectEntities == null)
            return Collections.emptyList();

        return subjectEntities.stream()
                .map(SubjectEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    public static SubjectEntity fromDomain(Subject subject) {

        if (subject == null)
            return null;

        return new SubjectEntity(
                subject.getId(),
                subject.getName(),
                subject.getDescription()
        );
    }

}
