package com.renan.booksalesonline.tests.adapters.controllers.v1.mappers;

import com.renan.booksalesonline.adapters.controllers.v1.mappers.PublicationImageDtoMapper;
import com.renan.booksalesonline.adapters.controllers.v1.model.PublicationImageDto;
import com.renan.booksalesonline.domain.PublicationImage;
import com.renan.booksalesonline.domain.enums.ImageExtension;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class PublicationImageDtoMapperTest {

    @Test
    public void should_parse_publication_image_dto_to_publication_image_domain() throws IOException {

        var instantDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        var dto = new MockMultipartFile(
                "file",
                "file.jpeg",
                "image/jpeg",
                (InputStream) null
        );
        var actual = PublicationImageDtoMapper.toDomain(dto);

        assertThat((int)actual.getId()).isEqualTo(0);
        assertThat(actual.getName()).isNotEmpty();
        assertThat(actual.getContentUrl()).isEmpty();
        assertThat(actual.getPublicationId()).isEqualTo(0);
        assertThat(actual.getImageContent()).isNotNull();
        assertThat(actual.getImageContent().contentName()).isEqualTo("file.jpeg");
        assertThat(actual.getImageContent().getContentDate()).isAfter(instantDate);
        assertThat(actual.getImageContent().contentLength()).isEqualTo(0);
        assertThat(actual.getImageContent().getContentExtension()).isEqualTo(ImageExtension.JPEG);
        assertThat(actual.getImageContent().contentStream()).isNotNull();
    }

    @Test
    public void should_parse_publication_image_domain_to_publication_image_dto() {

        var domain = new PublicationImage(1, "name", "url", 1);
        var expected = new PublicationImageDto("name", "url", 1);
        expected.setId(1);
        var actual = PublicationImageDtoMapper.fromDomain(domain);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_parse_country_entities_to_countries() {

        var entities = Arrays.asList(
                new PublicationImage(1, "name1", "url1", 1),
                new PublicationImage(2, "name2", "url2", 1),
                new PublicationImage(3, "name3", "url3", 1)
        );
        var expected = new PublicationImageDto[] {
                new PublicationImageDto("name1", "url1", 1),
                new PublicationImageDto("name2", "url2", 1),
                new PublicationImageDto("name3", "url3", 1)
        };
        expected[0].setId(1);
        expected[1].setId(2);
        expected[2].setId(3);

        var actual = PublicationImageDtoMapper.fromDomain(entities);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
