
package io.helidon.microstream;

import java.util.Arrays;
import java.util.Collections;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;


@Path("/")
@RequestScoped
public class MicrostreamResource {

    private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());


    private final MicrostreamService microstreamService;

    @Inject
    public MicrostreamResource(MicrostreamService service) {
        this.microstreamService = service;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getAllItems() {
        return createResponse(Arrays.toString(microstreamService.getAllItems().toArray()));
    }


    @Path("/{index}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getItem(@PathParam("index") String index) {
        microstreamService.getItem(Integer.valueOf(index));
        return createResponse(microstreamService.getItem(Integer.valueOf(index)));
    }

    @Path("/{text}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject addItem(@PathParam("text") String text) {
        microstreamService.addNewItem(text);
        return createResponse(Arrays.toString(microstreamService.getAllItems().toArray()));
    }

    @Path("/{index}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deteteItem(@PathParam("index") String index) {
        if (microstreamService.deleteItem(Integer.valueOf(index))) {
            return Response.ok(createResponse(Arrays.toString(microstreamService.getAllItems().toArray()))).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }


    private JsonObject createResponse(String item) {
        String msg = String.format("Result: %s!", item);

        return JSON.createObjectBuilder()
                .add("message", msg)
                .build();
    }
}
