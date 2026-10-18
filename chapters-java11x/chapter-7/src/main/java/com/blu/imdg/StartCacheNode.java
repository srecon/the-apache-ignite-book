package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Starts an Ignite node for cache operations.
 * <p>
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.StartCacheNode
 */
public class StartCacheNode {
    private static final Logger log = LoggerFactory.getLogger(StartCacheNode.class);

    public static void main(String[] args) {
        try (var ignite = Ignition.start("org/book/examples/cache-node-config.xml")) {
            log.info("Ignite node started. Press ENTER to exit!");
            System.in.read();
        } catch (IOException e) {
            log.error("Failed to read input", e);
        }
    }
}
