package com.renan.booksalesonline.application.ports.out.publisher;

import com.renan.booksalesonline.domain.Publisher;

public interface PublisherCommand {

    Publisher save(Publisher publisher);
    void remove(Publisher publisher);
}
