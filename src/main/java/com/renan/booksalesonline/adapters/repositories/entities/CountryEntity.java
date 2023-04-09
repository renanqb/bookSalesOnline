package com.renan.booksalesonline.adapters.repositories.entities;

import javax.persistence.*;
@Entity
@Table(name = "country")
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gentilic", nullable = false)
    private String gentilic;

    protected CountryEntity() {
        this(0, "", "");
    }

    public CountryEntity(int id, String name, String gentilic) {
        setId(id);
        setName(name);
        setGentilic(gentilic);
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

    public String getGentilic() {
        return gentilic;
    }

    public void setGentilic(String gentilic) {
        this.gentilic = gentilic;
    }
}
