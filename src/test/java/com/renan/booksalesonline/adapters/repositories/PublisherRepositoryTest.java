package com.renan.booksalesonline.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.data.PublisherData;
import com.renan.booksalesonline.adapters.repositories.entities.CountryEntity;
import com.renan.booksalesonline.adapters.repositories.entities.PublisherEntity;
import com.renan.booksalesonline.domain.Country;
import com.renan.booksalesonline.domain.Publisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublisherRepositoryTest {

    @Mock private PublisherData publisherData;
    @InjectMocks private PublisherRepository publisherRepository;

    @Test
    public void should_get_all_publisher_entities() {

        var countryEntity = new CountryEntity(1, "name", "gentilic");
        var publisherEntities = Arrays.asList(
                new PublisherEntity(1, "name1", "history1", countryEntity),
                new PublisherEntity(2, "name2", "history2", countryEntity),
                new PublisherEntity(3, "name3", "history3", countryEntity)
        );
        when(publisherData.findAll()).thenReturn(publisherEntities);

        var country = new Country(1, "name", "gentilic");
        var expectedPublishers = Arrays.asList(
                new Publisher(1, "name1", "history1", country),
                new Publisher(2, "name2", "history2", country),
                new Publisher(3, "name3", "history3", country)
        );

        var publishers = publisherRepository.getAll();

        for (var i = 0; i < 3; i++) {
            var expected = expectedPublishers.get(i);
            var actual = publishers.get(i);

            assertThat((int)actual.getId()).isEqualTo((int)expected.getId());
            assertThat(actual.getName()).isEqualTo(expected.getName());
            assertThat(actual.getHistory()).isEqualTo(expected.getHistory());
        }
    }

    @Test
    public void should_get_by_id_one_publisher_entity() {

        var mockedPublisherEntity = new PublisherEntity(1, "name", "history", null);
        when(publisherData.findById(anyInt())).thenReturn(Optional.of(mockedPublisherEntity));

        var expectedPublisher = new Publisher(1, "name", "history", null);
        var actualPublisher = publisherRepository.getById(1);

        assertThat((int)actualPublisher.getId()).isEqualTo((int)expectedPublisher.getId());
        assertThat(actualPublisher.getName()).isEqualTo(expectedPublisher.getName());
        assertThat(actualPublisher.getHistory()).isEqualTo(expectedPublisher.getHistory());
    }

    @Test
    public void should_create_a_publisher_entity() {

        var mockedPublisherEntity = new PublisherEntity(1, "name", "history", null);
        when(publisherData.save(any(PublisherEntity.class))).thenReturn(mockedPublisherEntity);

        var expectedPublisher = new Publisher(1, "name", "history", null);
        var actualPublisher = publisherRepository.save(expectedPublisher);

        assertThat((int)actualPublisher.getId()).isEqualTo((int)expectedPublisher.getId());
        assertThat(actualPublisher.getName()).isEqualTo(expectedPublisher.getName());
        assertThat(actualPublisher.getHistory()).isEqualTo(expectedPublisher.getHistory());
    }

    @Test
    public void should_remove_a_publisher_entity() {

        doNothing().when(publisherData).delete(any(PublisherEntity.class));

        assertDoesNotThrow(() -> {
            var publisher = new Publisher(1, "name", "history", null);
            publisherRepository.remove(publisher);
        });
    }

    @Test
    public void should_validate_whether_does_exists_publisher_by_country() {

        when(publisherData.existsPublisherByCountryId(anyInt())).thenReturn(true);
        assertThat(publisherRepository.existsPublisherByCountryId(1)).isTrue();
    }

    @Test
    public void should_validate_whether_does_not_exists_publisher_by_country() {

        when(publisherData.existsPublisherByCountryId(anyInt())).thenReturn(false);
        assertThat(publisherRepository.existsPublisherByCountryId(1)).isFalse();
    }

    @Test
    public void should_get_publisher_entities_from_a_given_country() {

        var countryEntity = new CountryEntity(1, "name", "gentilic");
        var publisherEntities = Arrays.asList(
                new PublisherEntity(1, "name1", "history1", countryEntity)
        );
        when(publisherData.getPublishersByCountryId(anyInt())).thenReturn(publisherEntities);

        var country = new Country(1, "name", "gentilic");
        var expectedPublishers = Arrays.asList(
                new Publisher(1, "name1", "history1", country)
        );

        var publishers = publisherRepository.getPublishersByCountryId(1);
        var expected = expectedPublishers.get(0);
        var actual = publishers.get(0);

        assertThat((int)actual.getId()).isEqualTo((int)expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getHistory()).isEqualTo(expected.getHistory());
        assertThat((int)actual.getCountry().getId()).isEqualTo((int)expected.getCountry().getId());
    }
}
