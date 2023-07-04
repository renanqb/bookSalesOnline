package com.renan.booksalesonline.adapters.controllers.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.renan.booksalesonline.adapters.controllers.v1.commom.BaseDto;

public class PublicationImageDto extends BaseDto {

    @JsonProperty("url") private String url;

    @JsonProperty("publicationId") private int publicationId;

    public PublicationImageDto() {
        this("", "", 0);
    }

    public PublicationImageDto(String name, String url, int publicationId) {
        super(name);
        this.url = url;
        this.publicationId = publicationId;
    }

    public String getUrl() {
        return url;
    }

    public int getPublicationId() {
        return publicationId;
    }
}
