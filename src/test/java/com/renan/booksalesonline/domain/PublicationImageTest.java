package com.renan.booksalesonline.domain;

import com.renan.booksalesonline.domain.exceptions.FileExtensionNotAccepted;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PublicationImageTest {

    @Test
    public void should_create_a_publication_image_domain_instance() throws IOException {

        var multipartFile = (MultipartFile) new MockMultipartFile(
                "name",
                "originalFilename.png",
                "contentType",
                (InputStream) null
        );
        var publicationImageContent = new PublicationImageContent(
                multipartFile.getOriginalFilename(),
                multipartFile.getSize(),
                multipartFile.getInputStream()
        );

        var publicationImage = new PublicationImage(publicationImageContent);
        assertThat((int)publicationImage.getId()).isEqualTo(0);
        assertThat(publicationImage.getName()).isNotEmpty();
        assertThat(publicationImage.getContentUrl()).isEmpty();
        assertThat(publicationImage.getPublicationId()).isEqualTo(0);
        assertThat(publicationImage.getImageContent()).isNotNull();
    }

    @Test
    public void should_create_a_publication_image_domain_instance_from_multipart_file() {

        var publicationImage = new PublicationImage(1, "name", "url", 10);
        assertThat((int)publicationImage.getId()).isEqualTo(1);
        assertThat(publicationImage.getName()).isEqualTo("name");
        assertThat(publicationImage.getContentUrl()).isEqualTo("url");
        assertThat(publicationImage.getPublicationId()).isEqualTo(10);
        assertThat(publicationImage.getImageContent()).isNull();
    }

    @Test
    public void should_validate_publication_image_extension() throws IOException {

        var multipartFile = (MultipartFile) new MockMultipartFile(
                "name",
                "originalFilename.jpeg",
                "contentType",
                (InputStream) null
        );
        var publicationImageContent = new PublicationImageContent(
                multipartFile.getOriginalFilename(),
                multipartFile.getSize(),
                multipartFile.getInputStream()
        );

        assertDoesNotThrow(publicationImageContent::validate);
    }

    @Test
    public void should_not_validate_publication_image_extension() throws IOException {

        var multipartFile = (MultipartFile) new MockMultipartFile(
                "name",
                "originalFilename.xxx",
                "contentType",
                (InputStream) null
        );
        var publicationImageContent = new PublicationImageContent(
                multipartFile.getOriginalFilename(),
                multipartFile.getSize(),
                multipartFile.getInputStream()
        );

        assertThrows(FileExtensionNotAccepted.class, publicationImageContent::validate);
    }
}
