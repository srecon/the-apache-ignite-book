package com.blu.imdg.common;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * HTTP client for sending audit results to the emulator service.
 */
public class HttpAuditClient {
    private static final Logger log = LoggerFactory.getLogger(HttpAuditClient.class);
    private static PoolingHttpClientConnectionManager connectionManager;

    public static Boolean sendResult(HttpClient client, Boolean result, String messageId) throws URISyntaxException, IOException {
        var uri = new URIBuilder()
                .setScheme("http")
                .setHost("localhost")
                .setPort(9998)
                .setPath("/audit/log")
                .setParameter("msgId", messageId)
                .setParameter("validationResult", result.toString())
                .build();

        try (var httpget = new HttpGet(uri);
             var resp = (CloseableHttpResponse) client.execute(httpget)) {
            log.info("msg={} result={}", messageId, result);
            return result;
        }
    }

    public static HttpClient createHttpClient(ConcurrentMap<Object, Object> nodeLocalMap) {
        return (HttpClient) nodeLocalMap.computeIfAbsent("httpClient", new Function<>() {
            @Override
            public HttpClient apply(Object o) {
                return createHttpClient();
            }
        });
    }

    public static HttpClient createHttpClient() {
        connectionManager = new PoolingHttpClientConnectionManager();
        log.info("Created HTTP client with connection pool (max total={}, default per route={})",
                connectionManager.getMaxTotal(), connectionManager.getDefaultMaxPerRoute());
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    /**
     * Shutdown the shared connection manager to release resources.
     */
    public static void shutdown() {
        if (connectionManager != null && connectionManager.isShutdown()) {
            connectionManager.shutdown();
            log.info("HTTP connection manager shut down");
        }
    }
}
