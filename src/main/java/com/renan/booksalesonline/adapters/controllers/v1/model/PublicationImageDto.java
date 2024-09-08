package com.renan.booksalesonline.adapters.controllers.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.renan.booksalesonline.adapters.controllers.v1.commom.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class PublicationImageDto extends BaseDto {

    @JsonProperty("url")
    private final String url;

    @JsonProperty("publicationId")
    private final int publicationId;

    public PublicationImageDto(String name, String url, int publicationId) {
        super(name);
        this.url = url;
        this.publicationId = publicationId;
    }
}
