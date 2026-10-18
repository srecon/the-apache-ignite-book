package com.blu.imdg.common;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * HTTP endpoint for receiving audit log messages from Ignite services.
 * <p>
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.common.HttpAuditEmulator
 */
@Path("/audit")
public class HttpAuditEmulator {
    private static final Logger log = LoggerFactory.getLogger(HttpAuditEmulator.class);


    @GET
    @Path("/log")
    @Produces(MediaType.TEXT_PLAIN)
    public String acceptResponse(
            @QueryParam("msgId") String msgId,
            @QueryParam("validationResult") boolean result
    ) {
        log.info("log message validation result msgId={} result={}", msgId, result);
        return "1";
    }

    public static void main(String[] args) throws IOException {
        var httpServer = HttpServer.create(new InetSocketAddress("localhost", 9998), 0);
        httpServer.start();
        log.info("Http Server on localhost:9998");
    }
}
