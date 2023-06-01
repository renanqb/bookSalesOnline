package com.renan.booksalesonline.application.ports.in.usecases.publisher;

import com.renan.booksalesonline.domain.Publisher;

public interface UpdatePublisherUseCase {

    Publisher execute(Publisher publisher, int id) throws NoSuchMethodException;
}
