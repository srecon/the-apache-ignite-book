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
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Isolated cluster example using Java 21 features.
 */
public class IsolatedCluster {
    private static final Logger log = LoggerFactory.getLogger(IsolatedCluster.class);

    public static void main(String[] args) {
        var cfg = new IgniteConfiguration();

        // Configure TCP discovery SPI with addresses from the first cluster.
        var discoverySpi = new TcpDiscoverySpi();
        discoverySpi.setLocalPort(48500);
        discoverySpi.setLocalPortRange(20);

        var ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(List.of("127.0.0.1:48500..48520"));
        discoverySpi.setIpFinder(ipFinder);

        // Configure TCP communication SPI with different local port range.
        var commSpi = new TcpCommunicationSpi();
        commSpi.setLocalPort(48100);

        cfg.setDiscoverySpi(discoverySpi);
        cfg.setCommunicationSpi(commSpi);
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);

        try (var ignite = Ignition.start(cfg)) {
            var cache = ignite.getOrCreateCache("myCacheSource");

            // Put some cache elements
            for (int i = 100; i <= 150; i++) {
                cache.put("Key:%d".formatted(i), "Hello World!!: %d".formatted(i));
            }
        }
    }
}
