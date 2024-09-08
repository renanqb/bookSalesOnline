package com.renan.booksalesonline.tests.domain;

import com.renan.booksalesonline.domain.PublicationImageContent;
import com.renan.booksalesonline.domain.enums.ImageExtension;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class PublicationImageContentTest {

    @Test
    public void should_create_a_publication_image_content_domain_instance() throws IOException {

        var instantDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
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

        assertThat(publicationImageContent.contentName()).isEqualTo(multipartFile.getOriginalFilename());
        assertThat(publicationImageContent.contentLength()).isEqualTo(0);
        assertThat(publicationImageContent.getContentDate()).isAfterOrEqualTo(instantDate);
        assertThat(publicationImageContent.getContentExtension()).isEqualTo(ImageExtension.PNG);
        assertThat(publicationImageContent.contentStream()).isNotNull();
    }
}
