package com.example.mammoth.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MammothFactTest {

    @Test
    void testNoArgConstructor() {
        MammothFact fact = new MammothFact();
        assertNull(fact.getCategory());
        assertNull(fact.getTitle());
        assertNull(fact.getDescription());
    }

    @Test
    void testParameterizedConstructor() {
        MammothFact fact = new MammothFact("Anatomy", "Curved Tusks", "Up to 14 feet long");
        assertEquals("Anatomy", fact.getCategory());
        assertEquals("Curved Tusks", fact.getTitle());
        assertEquals("Up to 14 feet long", fact.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        MammothFact fact = new MammothFact();
        fact.setCategory("Diet");
        fact.setTitle("Herbivores");
        fact.setDescription("Ate 400 pounds of vegetation daily");

        assertEquals("Diet", fact.getCategory());
        assertEquals("Herbivores", fact.getTitle());
        assertEquals("Ate 400 pounds of vegetation daily", fact.getDescription());
    }
}
