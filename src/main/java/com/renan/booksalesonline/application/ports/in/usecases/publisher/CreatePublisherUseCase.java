package com.renan.booksalesonline.application.ports.in.usecases.publisher;

import com.renan.booksalesonline.domain.Publisher;

public interface CreatePublisherUseCase  {

    Publisher execute(Publisher publisher) throws NoSuchMethodException;
}
