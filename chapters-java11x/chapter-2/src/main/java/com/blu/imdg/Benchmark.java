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
import org.apache.ignite.binary.BinaryField;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Binary object benchmark using Java 21 features.
 */
public class Benchmark {
    private static final Logger log = LoggerFactory.getLogger(Benchmark.class);

    public static void main(String[] args) {
        log.info("Hello Ignite");

        var spi = new TcpDiscoverySpi();
        var ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(List.of("127.0.0.1"));
        spi.setIpFinder(ipFinder);

        var cfg = new IgniteConfiguration();
        cfg.setIgniteInstanceName("Benchmark Node");
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);
        cfg.setDiscoverySpi(spi);

        try (var ignite = Ignition.start(cfg)) {
            var cache = ignite.cache("SQL_PUBLIC_EMP").withKeepBinary();

            BinaryObject binaryObject = (BinaryObject)cache.get(900L);
            if (binaryObject != null) {
                var enameField = binaryObject.type().field("ename");
                String ename = enameField.value(binaryObject);
                log.info("Ename: {}", ename);
            }
        }
    }
}
