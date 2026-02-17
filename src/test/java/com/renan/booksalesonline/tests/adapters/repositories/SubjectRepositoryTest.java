package com.renan.booksalesonline.tests.adapters.repositories;

import com.renan.booksalesonline.adapters.repositories.SubjectRepository;
import com.renan.booksalesonline.adapters.repositories.data.SubjectData;
import com.renan.booksalesonline.adapters.repositories.entities.SubjectEntity;
import com.renan.booksalesonline.domain.Subject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubjectRepositoryTest {

    @Mock private SubjectData subjectData;
    @InjectMocks private SubjectRepository subjectRepository;

    @Test
    public void should_get_all_subjects_with_pagination() {

        // Arrange
        var subjectEntity1 = new SubjectEntity(1, "Fantasia", "Livros de fantasia");
        var subjectEntity2 = new SubjectEntity(2, "Aventura", "Livros de aventura");
        var page = new PageImpl<>(Arrays.asList(subjectEntity1, subjectEntity2), PageRequest.of(0, 20), 2);

        when(subjectData.findAll(any(PageRequest.class))).thenReturn(page);

        // Act
        var subjects = subjectRepository.getAll(0, 20);

        // Assert
        assertThat(subjects).hasSize(2);
        assertThat(subjects.get(0).getId()).isEqualTo(1);
        assertThat(subjects.get(0).getName()).isEqualTo("Fantasia");
        assertThat(subjects.get(1).getId()).isEqualTo(2);
        assertThat(subjects.get(1).getName()).isEqualTo("Aventura");
    }

    @Test
    public void should_get_subject_by_id() {

        // Arrange
        var subjectEntity = new SubjectEntity(1, "Fantasia", "Livros de fantasia");
        when(subjectData.findById(1)).thenReturn(Optional.of(subjectEntity));

        // Act
        var subject = subjectRepository.getById(1);

        // Assert
        assertThat(subject).isNotNull();
        assertThat(subject.getId()).isEqualTo(1);
        assertThat(subject.getName()).isEqualTo("Fantasia");
        assertThat(subject.getDescription()).isEqualTo("Livros de fantasia");
    }

    @Test
    public void should_return_null_when_subject_not_found() {

        // Arrange
        when(subjectData.findById(999)).thenReturn(Optional.empty());

        // Act
        var subject = subjectRepository.getById(999);

        // Assert
        assertThat(subject).isNull();
    }

    @Test
    public void should_save_new_subject() {

        // Arrange
        var subjectDomain = new Subject(0, "Ficção Científica", "Livros de ficção científica");
        var subjectEntity = new SubjectEntity(0, "Ficção Científica", "Livros de ficção científica");
        var savedEntity = new SubjectEntity(3, "Ficção Científica", "Livros de ficção científica");

        when(subjectData.save(any(SubjectEntity.class))).thenReturn(savedEntity);

        // Act
        var result = subjectRepository.save(subjectDomain);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3);
        assertThat(result.getName()).isEqualTo("Ficção Científica");
        assertThat(result.getDescription()).isEqualTo("Livros de ficção científica");
    }

    @Test
    public void should_update_existing_subject() {

        // Arrange
        var subjectDomain = new Subject(1, "Fantasia Atualizada", "Descrição atualizada");
        var savedEntity = new SubjectEntity(1, "Fantasia Atualizada", "Descrição atualizada");

        when(subjectData.save(any(SubjectEntity.class))).thenReturn(savedEntity);

        // Act
        var result = subjectRepository.save(subjectDomain);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Fantasia Atualizada");
        assertThat(result.getDescription()).isEqualTo("Descrição atualizada");
    }

    @Test
    public void should_remove_subject() {

        // Arrange
        var subjectDomain = new Subject(1, "Fantasia", "Livros de fantasia");
        doNothing().when(subjectData).delete(any(SubjectEntity.class));

        // Act & Assert (no exception should be thrown)
        subjectRepository.remove(subjectDomain);
    }

    @Test
    public void should_map_subject_entity_to_domain_correctly() {

        // Arrange
        var subjectEntity = new SubjectEntity(5, "Romance", "Livros de romance");
        when(subjectData.findById(5)).thenReturn(Optional.of(subjectEntity));

        // Act
        var subject = subjectRepository.getById(5);

        // Assert - verify bidirectional mapping
        assertThat(subject).isNotNull();
        assertThat(subject.getId()).isEqualTo(5);
        assertThat(subject.getName()).isEqualTo("Romance");
        assertThat(subject.getDescription()).isEqualTo("Livros de romance");
    }

    @Test
    public void should_save_subject_with_all_fields() {

        // Arrange
        var subjectDomain = new Subject(0, "Terror", "Livros de terror e suspense");
        var savedEntity = new SubjectEntity(4, "Terror", "Livros de terror e suspense");

        when(subjectData.save(any(SubjectEntity.class))).thenReturn(savedEntity);

        // Act
        var result = subjectRepository.save(subjectDomain);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(4);
        assertThat(result.getName()).isEqualTo("Terror");
        assertThat(result.getDescription()).isEqualTo("Livros de terror e suspense");
    }
}

