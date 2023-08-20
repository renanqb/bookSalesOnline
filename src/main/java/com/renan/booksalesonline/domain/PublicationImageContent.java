package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.enums.ImageExtension;
import com.renan.booksalesonline.domain.exceptions.FileExtensionNotAccepted;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

public class PublicationImageContent {

    private String contentName;
    private long contentLength;
    private InputStream contentStream;

    public PublicationImageContent(
            String contentName, long contentLength, InputStream contentStream
    ) {
        this.contentName = contentName;
        this.contentLength = contentLength;
        this.contentStream = contentStream;
    }

    public String getContentName() {
        return contentName;
    }

    public long getContentLength() {
        return contentLength;
    }

    public InputStream getContentStream() {
        return contentStream;
    }

    public ImageExtension getContentExtension() {

        var indexOf = getContentName().lastIndexOf(".");
        var extension = getContentName().substring(indexOf)
                .replace(".", "")
                .toUpperCase(Locale.ROOT)
                .trim();

        return ImageExtension.valueOf(extension);
    }

    public Date getContentDate() {

        return Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    }

    public void validate() throws FileExtensionNotAccepted {

        try {
            getContentExtension();
        } catch (IllegalArgumentException e) {
            throw new FileExtensionNotAccepted();
        }
    }
}
