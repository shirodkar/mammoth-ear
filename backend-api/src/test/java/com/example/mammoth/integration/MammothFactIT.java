package com.example.mammoth.integration;

import com.example.mammoth.model.MammothFact;
import org.junit.jupiter.api.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Persistence;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MammothFactIT {

    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    static void setUpFactory() {
        emf = Persistence.createEntityManagerFactory("MammothPU");
    }

    @AfterAll
    static void tearDownFactory() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM MammothFact").executeUpdate();
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (em != null && em.isOpen()) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
        }
    }

    private MammothFact insertFact(String category, String title, String description) {
        em.getTransaction().begin();
        MammothFact fact = new MammothFact(category, title, description);
        em.persist(fact);
        em.getTransaction().commit();
        return fact;
    }

    @Test
    void testPersistAndFindById() {
        MammothFact fact = insertFact("Anatomy", "Curved Tusks", "Tusks could grow up to 14 feet");

        assertNotNull(fact.getId());

        em.clear();
        MammothFact found = em.find(MammothFact.class, fact.getId());

        assertNotNull(found);
        assertEquals("Anatomy", found.getCategory());
        assertEquals("Curved Tusks", found.getTitle());
        assertEquals("Tusks could grow up to 14 feet", found.getDescription());
    }

    @Test
    void testFindAll_orderedByCategoryThenId() {
        insertFact("Diet", "Plants", "Ate plants");
        insertFact("Anatomy", "Tusks", "Long tusks");
        insertFact("Diet", "Grass", "Ate grass");

        em.clear();
        List<MammothFact> results = em.createNamedQuery("MammothFact.findAll", MammothFact.class)
                .getResultList();

        assertEquals(3, results.size());
        assertEquals("Anatomy", results.get(0).getCategory());
        assertEquals("Diet", results.get(1).getCategory());
        assertEquals("Diet", results.get(2).getCategory());
        assertEquals("Plants", results.get(1).getTitle());
        assertEquals("Grass", results.get(2).getTitle());
    }

    @Test
    void testFindAll_emptyTable() {
        List<MammothFact> results = em.createNamedQuery("MammothFact.findAll", MammothFact.class)
                .getResultList();

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testFindByCategory_matchingFacts() {
        insertFact("Anatomy", "Tusks", "Long tusks");
        insertFact("Anatomy", "Coat", "Thick woolly coat");
        insertFact("Diet", "Plants", "Ate plants");

        em.clear();
        List<MammothFact> results = em.createNamedQuery("MammothFact.findByCategory", MammothFact.class)
                .setParameter("category", "Anatomy")
                .getResultList();

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(f -> "Anatomy".equals(f.getCategory())));
    }

    @Test
    void testFindByCategory_noMatch() {
        insertFact("Anatomy", "Tusks", "Long tusks");

        List<MammothFact> results = em.createNamedQuery("MammothFact.findByCategory", MammothFact.class)
                .setParameter("category", "Nonexistent")
                .getResultList();

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void testFindByCategory_caseSensitive() {
        insertFact("Anatomy", "Tusks", "Long tusks");

        List<MammothFact> results = em.createNamedQuery("MammothFact.findByCategory", MammothFact.class)
                .setParameter("category", "anatomy")
                .getResultList();

        assertTrue(results.isEmpty());
    }

    @Test
    void testFindCategories_distinctAndOrdered() {
        insertFact("Diet", "Plants", "Ate plants");
        insertFact("Anatomy", "Tusks", "Long tusks");
        insertFact("Diet", "Grass", "Ate grass");
        insertFact("Habitat", "Steppe", "Mammoth steppe");

        em.clear();
        List<String> categories = em.createNamedQuery("MammothFact.findCategories", String.class)
                .getResultList();

        assertEquals(3, categories.size());
        assertEquals("Anatomy", categories.get(0));
        assertEquals("Diet", categories.get(1));
        assertEquals("Habitat", categories.get(2));
    }

    @Test
    void testFindCategories_emptyTable() {
        List<String> categories = em.createNamedQuery("MammothFact.findCategories", String.class)
                .getResultList();

        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    void testEntityFieldConstraints_categoryNotNull() {
        assertThrows(PersistenceException.class, () -> {
            em.getTransaction().begin();
            MammothFact fact = new MammothFact(null, "Title", "Description");
            em.persist(fact);
            em.flush();
        });
    }

    @Test
    void testEntityFieldConstraints_titleNotNull() {
        assertThrows(PersistenceException.class, () -> {
            em.getTransaction().begin();
            MammothFact fact = new MammothFact("Category", null, "Description");
            em.persist(fact);
            em.flush();
        });
    }

    @Test
    void testEntityFieldConstraints_descriptionNotNull() {
        assertThrows(PersistenceException.class, () -> {
            em.getTransaction().begin();
            MammothFact fact = new MammothFact("Category", "Title", null);
            em.persist(fact);
            em.flush();
        });
    }
}
