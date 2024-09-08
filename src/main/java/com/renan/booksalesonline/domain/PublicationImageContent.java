package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.enums.ImageExtension;
import com.renan.booksalesonline.domain.exceptions.FileExtensionNotAccepted;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

public record PublicationImageContent(String contentName, long contentLength, InputStream contentStream) {

    public ImageExtension getContentExtension() {

        var indexOf = contentName().lastIndexOf(".");
        var extension = contentName().substring(indexOf)
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
