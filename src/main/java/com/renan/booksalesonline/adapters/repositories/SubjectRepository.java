package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.SubjectData;
import com.renan.booksalesonline.adapters.repositories.mappers.SubjectEntityMapper;
import com.renan.booksalesonline.application.ports.out.DataCommand;
import com.renan.booksalesonline.application.ports.out.publisher.SubjectDataQuery;
import com.renan.booksalesonline.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubjectRepository implements SubjectDataQuery, DataCommand<Subject> {

    private SubjectData subjectData;

    public SubjectRepository(@Autowired SubjectData subjectData) {this.subjectData = subjectData; }

    @Override
    public List<Subject> getAll() {

        var subjectEntities = subjectData.findAll();
        return SubjectEntityMapper.toDomain(subjectEntities);

    }

    @Override
    public Subject getById(int id) {
        return null;
    }

    @Override
    public Subject save(Subject domain) {
        return null;
    }

    @Override
    public void remove(Subject domain) {

    }
}
