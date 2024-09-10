package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Publication extends BaseDomain {

    private Publisher publisher;

    public Publication(int id, String name, Publisher publisher) {
        super(id, name);
        setPublisher(publisher);
    }

}
