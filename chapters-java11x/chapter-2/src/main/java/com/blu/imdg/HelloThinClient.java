package com.blu.imdg;

import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloThinClient {
    private static final Logger logger = LoggerFactory.getLogger(HelloThinClient.class);
    private static final String HOST = "127.0.0.1";
    private static final String PORT = "10800";
    private static final String CACHE_NAME= "thin-cache";

    public static void main(String[] args) {
        logger.info("Simple Ignite thin client example working over TCP socket.");

        ClientConfiguration cfg = new ClientConfiguration().setAddresses(HOST+":"+PORT);

        try (IgniteClient igniteClient = Ignition.startClient(cfg)) {

            ClientCache<String, String> clientCache = igniteClient.getOrCreateCache(CACHE_NAME);
            // put a few value
            clientCache.put("Moscow", "095");
            clientCache.put("Vladimir", "033");
            // get the region code of the Vladimir
            String val = clientCache.get("Vladimir");

            logger.info("Print value: {}", val);

        } catch(IgniteException e){

            logger.error("Ignite exception:", e.getMessage());

        } catch(Exception e){

            logger.error("Ignite exception:", e.getMessage());

        }

    }
}
