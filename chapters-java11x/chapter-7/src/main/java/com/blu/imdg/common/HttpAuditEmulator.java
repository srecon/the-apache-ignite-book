package com.blu.imdg.common;

import com.sun.net.httpserver.HttpServer;
//import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 *
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.common.HttpAuditEmulator
 */
@Path("/audit")
public class HttpAuditEmulator {


    @GET
    @Path("/log")
    @Produces(MediaType.TEXT_PLAIN)
    public String acceptResponse(
            @QueryParam("msgId") String msgId,
            @QueryParam("validationResult") boolean result
    ) {
        System.out.println("log message validation result msgId=" + msgId + " result=" + result);
        return "1";
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 9998),0);
        httpServer.start();
        System.out.println("Http Server on localhost:9998");
    }
}
