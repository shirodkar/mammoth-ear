package com.example.mammoth.web;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FactCategoryTest {

    @Test
    void testNoArgConstructor() {
        FactCategory category = new FactCategory();
        assertNull(category.getName());
        assertNull(category.getFacts());
    }

    @Test
    void testParameterizedConstructor() {
        List<MammothFact> facts = Arrays.asList(
                new MammothFact("Anatomy", "Tusks", "Long tusks")
        );
        FactCategory category = new FactCategory("Anatomy", facts);

        assertEquals("Anatomy", category.getName());
        assertEquals(1, category.getFacts().size());
    }

    @Test
    void testSettersAndGetters() {
        FactCategory category = new FactCategory();
        List<MammothFact> facts = Arrays.asList(
                new MammothFact("Diet", "Plants", "Ate plants")
        );
        category.setName("Diet");
        category.setFacts(facts);

        assertEquals("Diet", category.getName());
        assertEquals(facts, category.getFacts());
    }

    @Test
    void testFactsListIsSameReference() {
        List<MammothFact> facts = Arrays.asList(
                new MammothFact("Anatomy", "Tusks", "Long tusks")
        );
        FactCategory category = new FactCategory("Anatomy", facts);

        assertSame(facts, category.getFacts());
    }
}
