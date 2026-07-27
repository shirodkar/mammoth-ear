package com.example.mammoth.service;

import com.example.mammoth.model.MammothFact;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MammothFactService {

    @PersistenceContext(unitName = "MammothPU")
    private EntityManager em;

    public List<MammothFact> findAll() {
        return em.createNamedQuery("MammothFact.findAll", MammothFact.class).getResultList();
    }

    public List<MammothFact> findByCategory(String category) {
        return em.createNamedQuery("MammothFact.findByCategory", MammothFact.class)
                .setParameter("category", category)
                .getResultList();
    }

    public List<String> findCategories() {
        return em.createNamedQuery("MammothFact.findCategories", String.class).getResultList();
    }
}