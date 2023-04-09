package com.renan.booksalesonline.application.ports.in.publisher;

import com.renan.booksalesonline.domain.Publisher;

import java.util.List;

public interface GetAllPublishersUseCase {
    List<Publisher> execute();
}
