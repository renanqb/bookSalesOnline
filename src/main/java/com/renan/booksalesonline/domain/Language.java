package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;

public class Language extends BaseDomain {

    public Language() {
        this(0, "");
    }

    public Language(int id, String name) {
        super(id, name);
    }
}
