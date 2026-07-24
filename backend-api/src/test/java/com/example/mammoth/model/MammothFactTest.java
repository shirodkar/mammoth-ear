package com.example.mammoth.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MammothFactTest {

    @Test
    void testNoArgConstructor() {
        MammothFact fact = new MammothFact();
        assertNotNull(fact);
        assertNull(fact.getId());
        assertNull(fact.getCategory());
        assertNull(fact.getTitle());
        assertNull(fact.getDescription());
    }

    @Test
    void testParameterizedConstructor() {
        MammothFact fact = new MammothFact("Diet", "Big Eaters", "Ate 400 pounds daily");
        assertNull(fact.getId());
        assertEquals("Diet", fact.getCategory());
        assertEquals("Big Eaters", fact.getTitle());
        assertEquals("Ate 400 pounds daily", fact.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        MammothFact fact = new MammothFact();
        fact.setCategory("Anatomy");
        fact.setTitle("Curved Tusks");
        fact.setDescription("Tusks could grow up to 14 feet");

        assertEquals("Anatomy", fact.getCategory());
        assertEquals("Curved Tusks", fact.getTitle());
        assertEquals("Tusks could grow up to 14 feet", fact.getDescription());
    }

    @Test
    void testSetId() {
        MammothFact fact = new MammothFact();
        fact.setId(42L);
        assertEquals(42L, fact.getId());
    }
}
