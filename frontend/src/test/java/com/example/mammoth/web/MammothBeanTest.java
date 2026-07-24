package com.example.mammoth.web;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class MammothBeanTest {

    private static Level originalLevel;
    private String originalProperty;

    @BeforeAll
    static void suppressBeanLogger() {
        Logger logger = Logger.getLogger(MammothBean.class.getName());
        originalLevel = logger.getLevel();
        logger.setLevel(Level.OFF);
    }

    @AfterAll
    static void restoreBeanLogger() {
        Logger.getLogger(MammothBean.class.getName()).setLevel(originalLevel);
    }

    @BeforeEach
    void setUp() {
        originalProperty = System.getProperty("mammoth.api.url");
    }

    @AfterEach
    void tearDown() {
        if (originalProperty != null) {
            System.setProperty("mammoth.api.url", originalProperty);
        } else {
            System.clearProperty("mammoth.api.url");
        }
    }

    @Test
    void testInit_fallbackWhenBackendUnavailable() {
        MammothBean bean = new MammothBean();
        bean.init();

        assertNotNull(bean.getDescription());
        assertTrue(bean.getDescription().contains("Unable to connect"));
    }

    @Test
    void testInit_fallbackTaxonomy() {
        MammothBean bean = new MammothBean();
        bean.init();

        Map<String, String> taxonomy = bean.getTaxonomy();
        assertEquals(4, taxonomy.size());
        assertEquals("Mammuthus", taxonomy.get("Scientific Name"));
        assertEquals("Animalia", taxonomy.get("Kingdom"));
        assertEquals("Proboscidea", taxonomy.get("Order"));
        assertEquals("Elephantidae", taxonomy.get("Family"));
    }

    @Test
    void testInit_fallbackFactCategoriesEmpty() {
        MammothBean bean = new MammothBean();
        bean.init();

        List<FactCategory> categories = bean.getFactCategories();
        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    void testGetTaxonomyEntries_matchesTaxonomyMap() {
        MammothBean bean = new MammothBean();
        bean.init();

        List<Map.Entry<String, String>> entries = bean.getTaxonomyEntries();
        Map<String, String> taxonomy = bean.getTaxonomy();

        assertEquals(taxonomy.size(), entries.size());
        for (Map.Entry<String, String> entry : entries) {
            assertEquals(taxonomy.get(entry.getKey()), entry.getValue());
        }
    }

    @Test
    void testGetApiBaseUrl_systemPropertyTakesPrecedence() throws Exception {
        System.setProperty("mammoth.api.url", "http://custom:9090/api");

        MammothBean bean = new MammothBean();
        String url = invokeGetApiBaseUrl(bean);

        assertEquals("http://custom:9090/api", url);
    }

    @Test
    void testGetApiBaseUrl_defaultValueWhenNothingSet() throws Exception {
        System.clearProperty("mammoth.api.url");

        MammothBean bean = new MammothBean();
        String url = invokeGetApiBaseUrl(bean);

        assertEquals("http://localhost:8080/backend/api/mammoths", url);
    }

    @Test
    void testInit_taxonomyIsLinkedHashMap() {
        MammothBean bean = new MammothBean();
        bean.init();

        assertInstanceOf(LinkedHashMap.class, bean.getTaxonomy());
    }

    @Test
    void testInit_fieldsNotNullAfterInit() {
        MammothBean bean = new MammothBean();
        bean.init();

        assertNotNull(bean.getDescription());
        assertNotNull(bean.getTaxonomy());
        assertNotNull(bean.getFactCategories());
    }

    private String invokeGetApiBaseUrl(MammothBean bean) throws Exception {
        Method method = MammothBean.class.getDeclaredMethod("getApiBaseUrl");
        method.setAccessible(true);
        return (String) method.invoke(bean);
    }
}
