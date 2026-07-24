package com.example.mammoth.web;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("mammothBean")
@RequestScoped
public class MammothBean {

    private static final Logger LOGGER = Logger.getLogger(MammothBean.class.getName());

    private String description;
    private Map<String, String> taxonomy;
    private List<FactCategory> factCategories;

    private String getApiBaseUrl() {
        String url = System.getProperty("mammoth.api.url");
        if (url == null || url.isEmpty()) {
            url = System.getenv("MAMMOTH_API_URL");
        }
        if (url == null || url.isEmpty()) {
            url = "http://localhost:8080/backend/api/mammoths";
        }
        return url;
    }

    @PostConstruct
    public void init() {
        taxonomy = new LinkedHashMap<>();
        factCategories = new ArrayList<>();

        Client client = null;
        try {
            client = ClientBuilder.newClient();
            String baseUrl = getApiBaseUrl();

            loadOverview(client, baseUrl);
            loadFacts(client, baseUrl);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load data from backend API, using fallback data", e);
            loadFallbackData();
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

    private void loadOverview(Client client, String baseUrl) {
        Response response = client.target(baseUrl + "/overview")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == 200) {
            String json = response.readEntity(String.class);
            try (JsonReader reader = Json.createReader(new StringReader(json))) {
                JsonObject obj = reader.readObject();
                description = obj.getString("description", "");
                taxonomy.put("Scientific Name", obj.getString("scientificName", ""));
                taxonomy.put("Kingdom", obj.getString("kingdom", ""));
                taxonomy.put("Phylum", obj.getString("phylum", ""));
                taxonomy.put("Class", obj.getString("class", ""));
                taxonomy.put("Order", obj.getString("order", ""));
                taxonomy.put("Family", obj.getString("family", ""));
                taxonomy.put("Genus", obj.getString("genus", ""));
            }
        }
    }

    private void loadFacts(Client client, String baseUrl) {
        Response catResponse = client.target(baseUrl + "/categories")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (catResponse.getStatus() == 200) {
            String catJson = catResponse.readEntity(String.class);
            try (JsonReader reader = Json.createReader(new StringReader(catJson))) {
                JsonArray categories = reader.readArray();

                for (JsonString category : categories.getValuesAs(JsonString.class)) {
                    String categoryName = category.getString();

                    Response factsResponse = client.target(baseUrl + "/facts/" + categoryName)
                            .request(MediaType.APPLICATION_JSON)
                            .get();

                    if (factsResponse.getStatus() == 200) {
                        String factsJson = factsResponse.readEntity(String.class);
                        try (JsonReader factsReader = Json.createReader(new StringReader(factsJson))) {
                            JsonArray factsArray = factsReader.readArray();
                            List<MammothFact> facts = new ArrayList<>();

                            for (JsonObject factObj : factsArray.getValuesAs(JsonObject.class)) {
                                MammothFact fact = new MammothFact(
                                        factObj.getString("category", ""),
                                        factObj.getString("title", ""),
                                        factObj.getString("description", "")
                                );
                                facts.add(fact);
                            }

                            factCategories.add(new FactCategory(categoryName, facts));
                        }
                    }
                }
            }
        }
    }

    private void loadFallbackData() {
        description = "Mammoths were large, hairy elephants that roamed the Earth during the Pleistocene epoch. "
                + "Unable to connect to the backend API for full details.";
        taxonomy.put("Scientific Name", "Mammuthus");
        taxonomy.put("Kingdom", "Animalia");
        taxonomy.put("Order", "Proboscidea");
        taxonomy.put("Family", "Elephantidae");
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getTaxonomy() {
        return taxonomy;
    }

    public List<Map.Entry<String, String>> getTaxonomyEntries() {
        return new ArrayList<>(taxonomy.entrySet());
    }

    public List<FactCategory> getFactCategories() {
        return factCategories;
    }
}
