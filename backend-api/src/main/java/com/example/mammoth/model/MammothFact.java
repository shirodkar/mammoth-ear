package com.example.mammoth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mammoth_fact")
@NamedQueries({
    @NamedQuery(name = "MammothFact.findAll", query = "SELECT f FROM MammothFact f ORDER BY f.category, f.id"),
    @NamedQuery(name = "MammothFact.findByCategory", query = "SELECT f FROM MammothFact f WHERE f.category = :category ORDER BY f.id"),
    @NamedQuery(name = "MammothFact.findCategories", query = "SELECT DISTINCT f.category FROM MammothFact f ORDER BY f.category")
})
public class MammothFact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1024)
    private String description;

    public MammothFact() {
    }

    public MammothFact(String category, String title, String description) {
        this.category = category;
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
