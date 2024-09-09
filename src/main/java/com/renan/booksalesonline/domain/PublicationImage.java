package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PublicationImage extends BaseDomain<Integer> {

    private String contentUrl;
    private int publicationId;
    private PublicationImageContent imageContent;

    public PublicationImage(int id, String name, String url, int publicationId) {
        super(id, name);
        setContentUrl(url);
        setPublicationId(publicationId);
    }

    public PublicationImage(PublicationImageContent imageContent) {

        this(0, "", "", 0);
        this.imageContent = imageContent;
    }

    @Override
    public String getName() {

        var shouldSetName = super.getName().isEmpty() && getImageContent() != null;
        if (shouldSetName) {
            var newName = String.format(
                    "%s.%s",
                    UUID.randomUUID(),
                    getImageContent().getContentExtension().getText()
            );
            setName(newName);
        }
        return super.getName();
    }
}
