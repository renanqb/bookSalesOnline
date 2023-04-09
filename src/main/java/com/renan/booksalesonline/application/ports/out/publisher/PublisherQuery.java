package com.renan.booksalesonline.application.ports.out.publisher;

import com.renan.booksalesonline.domain.Publisher;

import java.util.List;

public interface PublisherQuery {

    List<Publisher> getAll();
    Publisher getById(int id);
}
