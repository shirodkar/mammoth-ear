package com.example.mammoth.api;

import com.example.mammoth.model.MammothFact;
import com.example.mammoth.service.MammothFactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MammothResourceTest {

    @Mock
    private MammothFactService factService;

    @InjectMocks
    private MammothResource resource;

    @Test
    void testGetOverview_returns200() {
        Response response = resource.getOverview();
        assertEquals(200, response.getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetOverview_containsExpectedKeys() {
        Response response = resource.getOverview();
        Map<String, String> overview = (Map<String, String>) response.getEntity();

        assertTrue(overview.containsKey("name"));
        assertTrue(overview.containsKey("scientificName"));
        assertTrue(overview.containsKey("kingdom"));
        assertTrue(overview.containsKey("phylum"));
        assertTrue(overview.containsKey("class"));
        assertTrue(overview.containsKey("order"));
        assertTrue(overview.containsKey("family"));
        assertTrue(overview.containsKey("genus"));
        assertTrue(overview.containsKey("description"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetOverview_correctValues() {
        Response response = resource.getOverview();
        Map<String, String> overview = (Map<String, String>) response.getEntity();

        assertEquals("Mammoth", overview.get("name"));
        assertEquals("Mammuthus", overview.get("scientificName"));
        assertEquals("Animalia", overview.get("kingdom"));
        assertEquals("Elephantidae", overview.get("family"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetAllFacts_returns200() {
        List<MammothFact> facts = Arrays.asList(
                new MammothFact("Anatomy", "Tusks", "Long tusks"),
                new MammothFact("Diet", "Plants", "Ate plants")
        );
        when(factService.findAll()).thenReturn(facts);

        Response response = resource.getAllFacts();

        assertEquals(200, response.getStatus());
        assertEquals(facts, response.getEntity());
    }

    @Test
    void testGetAllFacts_emptyList() {
        when(factService.findAll()).thenReturn(Collections.emptyList());

        Response response = resource.getAllFacts();

        assertEquals(200, response.getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetFactsByCategory_returns200() {
        List<MammothFact> facts = Arrays.asList(
                new MammothFact("Diet", "Big Eaters", "Ate a lot")
        );
        when(factService.findByCategory("Diet")).thenReturn(facts);

        Response response = resource.getFactsByCategory("Diet");

        assertEquals(200, response.getStatus());
        assertEquals(facts, response.getEntity());
    }

    @Test
    void testGetFactsByCategory_returns404WhenEmpty() {
        when(factService.findByCategory("Nonexistent")).thenReturn(Collections.emptyList());

        Response response = resource.getFactsByCategory("Nonexistent");

        assertEquals(404, response.getStatus());
    }

    @Test
    void testGetFactsByCategory_404ErrorMessageContainsCategory() {
        when(factService.findByCategory("Nonexistent")).thenReturn(Collections.emptyList());

        Response response = resource.getFactsByCategory("Nonexistent");

        String entity = (String) response.getEntity();
        assertTrue(entity.contains("Nonexistent"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetCategories_returns200() {
        List<String> categories = Arrays.asList("Anatomy", "Diet");
        when(factService.findCategories()).thenReturn(categories);

        Response response = resource.getCategories();

        assertEquals(200, response.getStatus());
        assertEquals(categories, response.getEntity());
    }

    @Test
    void testGetCategories_emptyList() {
        when(factService.findCategories()).thenReturn(Collections.emptyList());

        Response response = resource.getCategories();

        assertEquals(200, response.getStatus());
    }
}
