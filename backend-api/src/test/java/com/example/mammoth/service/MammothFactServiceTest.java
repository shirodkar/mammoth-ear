package com.example.mammoth.service;

import com.example.mammoth.model.MammothFact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MammothFactServiceTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<MammothFact> factQuery;

    @Mock
    private TypedQuery<String> stringQuery;

    @InjectMocks
    private MammothFactService service;

    @Test
    void testFindAll() {
        List<MammothFact> expected = Arrays.asList(
                new MammothFact("Anatomy", "Tusks", "Long curved tusks"),
                new MammothFact("Diet", "Plants", "Ate plants")
        );
        when(em.createNamedQuery("MammothFact.findAll", MammothFact.class)).thenReturn(factQuery);
        when(factQuery.getResultList()).thenReturn(expected);

        List<MammothFact> result = service.findAll();

        assertEquals(expected, result);
        verify(em).createNamedQuery("MammothFact.findAll", MammothFact.class);
    }

    @Test
    void testFindAll_emptyList() {
        when(em.createNamedQuery("MammothFact.findAll", MammothFact.class)).thenReturn(factQuery);
        when(factQuery.getResultList()).thenReturn(Collections.emptyList());

        List<MammothFact> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByCategory() {
        List<MammothFact> expected = Arrays.asList(
                new MammothFact("Diet", "Big Eaters", "Ate a lot")
        );
        when(em.createNamedQuery("MammothFact.findByCategory", MammothFact.class)).thenReturn(factQuery);
        when(factQuery.setParameter("category", "Diet")).thenReturn(factQuery);
        when(factQuery.getResultList()).thenReturn(expected);

        List<MammothFact> result = service.findByCategory("Diet");

        assertEquals(expected, result);
        verify(factQuery).setParameter("category", "Diet");
    }

    @Test
    void testFindByCategory_noResults() {
        when(em.createNamedQuery("MammothFact.findByCategory", MammothFact.class)).thenReturn(factQuery);
        when(factQuery.setParameter("category", "Nonexistent")).thenReturn(factQuery);
        when(factQuery.getResultList()).thenReturn(Collections.emptyList());

        List<MammothFact> result = service.findByCategory("Nonexistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindCategories() {
        List<String> expected = Arrays.asList("Anatomy", "Diet", "Habitat");
        when(em.createNamedQuery("MammothFact.findCategories", String.class)).thenReturn(stringQuery);
        when(stringQuery.getResultList()).thenReturn(expected);

        List<String> result = service.findCategories();

        assertEquals(expected, result);
        verify(em).createNamedQuery("MammothFact.findCategories", String.class);
    }
}
