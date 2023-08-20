package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.commom.BaseDomain;

import java.util.UUID;

public class PublicationImage extends BaseDomain {

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

    public void setContentUrl(String url) {
        contentUrl = url;
    }

    public String getContentUrl() { return contentUrl; }

    public void setPublicationId(int publicationId) { this.publicationId = publicationId; }

    public int getPublicationId() { return publicationId; }

    public PublicationImageContent getImageContent() { return imageContent; }
}
