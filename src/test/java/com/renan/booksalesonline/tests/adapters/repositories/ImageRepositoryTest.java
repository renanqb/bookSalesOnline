package com.renan.booksalesonline.tests.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.ImageRepository;
import com.renan.booksalesonline.adapters.repositories.data.ImageData;
import com.renan.booksalesonline.adapters.repositories.entities.ImageEntity;
import com.renan.booksalesonline.domain.PublicationImage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImageRepositoryTest {

    @Mock
    private ImageData imageData;
    @InjectMocks
    private ImageRepository imageRepository;

    @Test
    public void should_throw_not_implemented_exception_when_get_all() {

        assertThrows(UnsupportedOperationException.class, () -> imageRepository.getAll());
    }

    @Test
    public void should_get_by_id_one_image_entity() {

        var imageEntity = new ImageEntity(1, "name1", "url1", 10);
        when(imageData.findById(anyInt())).thenReturn(Optional.of(imageEntity));

        var expected = new PublicationImage(1, "name1", "url1", 10);
        var image = imageRepository.getById(1);

        assertThat((int)image.getId()).isEqualTo((int)expected.getId());
        assertThat(image.getName()).isEqualTo(expected.getName());
        assertThat(image.getContentUrl()).isEqualTo(expected.getContentUrl());
        assertThat(image.getPublicationId()).isEqualTo(expected.getPublicationId());
    }

    @Test
    public void should_create_an_image_entity() {

        var imageEntity = new ImageEntity(1, "name1", "url1", 10);
        when(imageData.save(any(ImageEntity.class))).thenReturn(imageEntity);

        var expected = new PublicationImage(1, "name1", "url1", 10);
        var image = imageRepository.save(expected);

        assertThat((int)image.getId()).isEqualTo((int)expected.getId());
        assertThat(image.getName()).isEqualTo(expected.getName());
        assertThat(image.getContentUrl()).isEqualTo(expected.getContentUrl());
        assertThat(image.getPublicationId()).isEqualTo(expected.getPublicationId());
    }

    @Test
    public void should_remove_an_image_entity() {

        doNothing().when(imageData).delete(any(ImageEntity.class));

        assertDoesNotThrow(() -> {
            var image = new PublicationImage(1, "name1", "url1", 10);
            imageRepository.remove(image);
        });
    }

    @Test
    public void should_get_image_entities_given_a_publication() {

        var imageEntities = List.of(new ImageEntity(1, "name1", "url1", 10));
        when(imageData.getImagesByPublicationId(anyInt())).thenReturn(imageEntities);

        var expectedImages = List.of(new PublicationImage(1, "name1", "url1", 10));

        var publicationImages = imageRepository.getImagesByPublicationId(10);
        var expected = expectedImages.get(0);
        var actual = publicationImages.get(0);

        assertThat((int)actual.getId()).isEqualTo((int)expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getContentUrl()).isEqualTo(expected.getContentUrl());
        assertThat(actual.getPublicationId()).isEqualTo(expected.getPublicationId());
    }
}
