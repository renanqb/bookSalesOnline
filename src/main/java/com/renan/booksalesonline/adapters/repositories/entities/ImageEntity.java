package com.renan.booksalesonline.adapters.repositories.entities;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "id_publication", nullable = false)
    private int publicationId;

    protected ImageEntity() {
        this(0, "", "", 0);
    }

    public ImageEntity(int id, String name, String url, int publicationId) {
        setId(id);
        setName(name);
        setUrl(url);
        setPublicationId(publicationId);
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPublicationId(int publicationId) {
        this.publicationId = publicationId;
    }

    public int getPublicationId() {
        return publicationId;
    }
}
