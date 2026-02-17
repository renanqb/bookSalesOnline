package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.SubjectData;
import com.renan.booksalesonline.adapters.repositories.mappers.SubjectEntityMapper;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.DataQuery;
import com.renan.booksalesonline.domain.Subject;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class SubjectRepository implements DataQuery<Subject>, DataCommand<Subject> {

    private final SubjectData subjectData;

    @Override
    public List<Subject> getAll(int page, int size) {

        var pageRequest = PageRequest.of(page, size);
        var subjectEntities = subjectData.findAll(pageRequest);
        return subjectEntities.map(SubjectEntityMapper::toDomain).toList();
    }

    @Override
    public Subject getById(int id) {

        var optSubjectEntity = subjectData.findById(id);
        return optSubjectEntity
                .map(SubjectEntityMapper::toDomain)
                .orElse(null);
    }

    @Override
    public Subject save(Subject subject) {

        var subjectEntity = SubjectEntityMapper.fromDomain(subject);
        var saved = subjectData.save(subjectEntity);
        subject.setId(saved.getId());

        return subject;
    }

    @Override
    public void remove(Subject subject) {

        var subjectEntity = SubjectEntityMapper.fromDomain(subject);
        subjectData.delete(subjectEntity);
    }
}

