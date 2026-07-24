package com.example.mammoth.api;

import com.example.mammoth.model.MammothFact;
import com.example.mammoth.service.MammothFactService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Path("/mammoths")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class MammothResource {

    @Inject
    private MammothFactService factService;

    @GET
    @Path("/overview")
    public Response getOverview() {
        Map<String, String> overview = new LinkedHashMap<>();
        overview.put("name", "Mammoth");
        overview.put("scientificName", "Mammuthus");
        overview.put("kingdom", "Animalia");
        overview.put("phylum", "Chordata");
        overview.put("class", "Mammalia");
        overview.put("order", "Proboscidea");
        overview.put("family", "Elephantidae");
        overview.put("genus", "Mammuthus");
        overview.put("description",
                "Mammoths were large, hairy elephants that roamed the Earth during the Pleistocene epoch. "
                + "They are among the most iconic creatures of the Ice Age, known for their impressive curved tusks, "
                + "thick woolly coats, and remarkable adaptation to cold climates. Multiple species existed, "
                + "including the woolly mammoth, Columbian mammoth, and steppe mammoth.");
        return Response.ok(overview).build();
    }

    @GET
    @Path("/facts")
    public Response getAllFacts() {
        List<MammothFact> facts = factService.findAll();
        return Response.ok(facts).build();
    }

    @GET
    @Path("/facts/{category}")
    public Response getFactsByCategory(@PathParam("category") String category) {
        List<MammothFact> facts = factService.findByCategory(category);
        if (facts.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"No facts found for category: " + category + "\"}")
                    .build();
        }
        return Response.ok(facts).build();
    }

    @GET
    @Path("/categories")
    public Response getCategories() {
        List<String> categories = factService.findCategories();
        return Response.ok(categories).build();
    }
}
