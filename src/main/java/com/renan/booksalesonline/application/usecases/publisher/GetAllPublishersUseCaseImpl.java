package com.renan.booksalesonline.application.usecases.publisher;

import com.renan.booksalesonline.application.ports.in.publisher.GetAllPublishersUseCase;
import com.renan.booksalesonline.application.ports.out.publisher.PublisherQuery;
import com.renan.booksalesonline.domain.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllPublishersUseCaseImpl implements GetAllPublishersUseCase {

    private PublisherQuery repository;

    public GetAllPublishersUseCaseImpl(@Autowired PublisherQuery publisherRepository) {
        this.repository = publisherRepository;
    }

    @Override
    public List<Publisher> execute() {
        return this.repository.getAll();
    }
}
