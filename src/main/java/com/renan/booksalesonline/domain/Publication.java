package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;

public class Publication extends BaseDomain {

    private Publisher publisher;

    public Publication() {
        this(0, "", null);
    }

    public Publication(int id, String name, Publisher publisher) {
        super(id, name);
        setPublisher(publisher);
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
