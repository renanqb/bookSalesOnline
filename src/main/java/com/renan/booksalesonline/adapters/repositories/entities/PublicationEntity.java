package com.renan.booksalesonline.adapters.repositories.entities;

import javax.persistence.*;

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

    protected PublicationEntity() {
        this(0, "", null);
    }

    public PublicationEntity(int id, String name, PublisherEntity publisher) {
        setId(id);
        setName(name);
        setPublisher(publisher);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PublisherEntity getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherEntity publisher) {
        this.publisher = publisher;
    }
}
