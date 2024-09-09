package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Language extends BaseDomain<Integer> {

    public Language() {
        this(0, "");
    }

    public Language(int id, String name) {
        super(id, name);
    }
}
