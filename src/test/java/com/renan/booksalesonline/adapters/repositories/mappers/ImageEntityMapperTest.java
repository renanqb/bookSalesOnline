package com.renan.booksalesonline.adapters.repositories.mappers;

import com.renan.booksalesonline.adapters.repositories.entities.ImageEntity;
import com.renan.booksalesonline.domain.PublicationImage;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageEntityMapperTest {

    @Test
    public void should_return_null_when_image_entity_is_null() {

        assertThat(ImageEntityMapper.toDomain((ImageEntity) null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_image_entity_to_publication_image_domain() {

        var entity = new ImageEntity(99, "name", "url", 1);
        var expected = new PublicationImage(99, "name", "url", 1);
        var actual = ImageEntityMapper.toDomain(entity);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_return_null_when_publication_image_domain_is_null() {

        assertThat(ImageEntityMapper.fromDomain(null))
                .usingRecursiveComparison()
                .isEqualTo(null);
    }

    @Test
    public void should_parse_publication_image_domain_to_image_entity() {

        var domain = new PublicationImage(99, "name", "url", 1);
        var expected = new ImageEntity(99, "name", "url", 1);
        var actual = ImageEntityMapper.fromDomain(domain);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void should_return_image_empty_list_when_domain_list_is_null() {

        assertThat(ImageEntityMapper.toDomain((List<ImageEntity>) null))
                .usingRecursiveComparison()
                .isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void should_parse_image_entities_to_publication_images() {

        var entities = Arrays.asList(
                new ImageEntity(1, "name1", "url1", 1),
                new ImageEntity(2, "name2", "url2", 1),
                new ImageEntity(3, "name3", "url3", 1)
        );
        var expected = Arrays.asList(
                new PublicationImage(1, "name1", "url1", 1),
                new PublicationImage(2, "name2", "url2", 1),
                new PublicationImage(3, "name3", "url3", 1)
        );
        var actual = ImageEntityMapper.toDomain(entities);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
