package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Publication extends BaseDomain<Integer> {

    private Publisher publisher;

    public Publication() {
        this(0, "", null);
    }

    public Publication(int id, String name, Publisher publisher) {
        super(id, name);
        setPublisher(publisher);
    }

}
