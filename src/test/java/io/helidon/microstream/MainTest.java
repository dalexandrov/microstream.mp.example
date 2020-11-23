package io.helidon.microstream;

import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.microprofile.server.Server;
import io.helidon.webclient.WebClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.spi.CDI;
import javax.json.JsonObject;

public class MainTest {

    private static Server server;
    private static String serverUrl;
    private static WebClient webClient;

    @BeforeAll
    public static void startTheServer() throws Exception {
        server = Server.create().start();
        serverUrl = "http://localhost:" + server.port();

        webClient = WebClient.builder()
                .baseUri("http://localhost:" + server.port())
                .addMediaSupport(JsonpSupport.create())
                .build();
    }

    @AfterAll
    public static void destroyClass() throws Exception {
        CDI<Object> current = CDI.current();
        ((SeContainer) current).close();

    }

    @Test
    void testMicrostreams() throws Exception {

        webClient.get()
                .path("/")
                .request(JsonObject.class)
                .thenAccept(jsonObject -> Assertions.assertEquals("Result: [Hello, World]!", jsonObject.getString("message")))
                .toCompletableFuture()
                .get();

        webClient.put()
                .path("/Helidon")
                .request(JsonObject.class)
                .thenAccept(jsonObject -> Assertions.assertEquals("Result: [Hello, World, Helidon]!", jsonObject.getString("message")))
                .toCompletableFuture()
                .get();

        webClient.delete()
                .path("/1")
                .request(JsonObject.class)
                .thenAccept(jsonObject -> Assertions.assertEquals("Result: [Hello, Helidon]!", jsonObject.getString("message")))
                .toCompletableFuture()
                .get();

        webClient.get()
                .path("/1")
                .request(JsonObject.class)
                .thenAccept(jsonObject -> Assertions.assertEquals("Result: Helidon!", jsonObject.getString("message")))
                .toCompletableFuture()
                .get();

        webClient.get()
                .path("/health")
                .request()
                .thenAccept(response -> Assertions.assertEquals(200, response.status().code()))
                .toCompletableFuture()
                .get();

        webClient.get()
                .path("/metrics")
                .request()
                .thenAccept(response -> Assertions.assertEquals(200, response.status().code()))
                .toCompletableFuture()
                .get();
    }
}
