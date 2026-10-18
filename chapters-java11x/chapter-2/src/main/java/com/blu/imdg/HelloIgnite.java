/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Basic Ignite node example using Java 21 features:
 * - try-with-resources for automatic cleanup
 * - List.of() for immutable collections
 * - var for type inference
 * - Logger instead of System.out.println
 */
public class HelloIgnite {
    private static final Logger log = LoggerFactory.getLogger(HelloIgnite.class);

    public static void main(String[] args) {
        log.info("Hello Ignite");

        // Configure TCP discovery SPI
        var spi = new TcpDiscoverySpi();
        var ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(List.of("localhost"));
        spi.setIpFinder(ipFinder);

        // Build configuration
        var cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("IgniteDataNode");
        cfg.setClientMode(false);
        cfg.setDiscoverySpi(spi);

        // Start Ignite with automatic cleanup
        try (var ignite = Ignition.start(cfg)) {
            // Get or create cache
            var cache = ignite.getOrCreateCache("testCache");

            // Put some cache elements
            for (int i = 1; i <= 100; i++) {
                cache.put(i, "Hello World: %d".formatted(i));
            }

            // Retrieve and log them
            for (int i = 1; i <= 100; i++) {
                log.info("Cache get: {}", cache.get(i));
            }
        }
    }
}
