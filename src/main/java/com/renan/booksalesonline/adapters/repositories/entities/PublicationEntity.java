package com.renan.booksalesonline.adapters.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "publication")
public class PublicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(
            name = "id_publisher",
            referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_publication_publisher")
    )
    private PublisherEntity publisher;

}
