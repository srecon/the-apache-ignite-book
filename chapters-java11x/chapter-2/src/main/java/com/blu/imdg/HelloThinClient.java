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

import org.apache.ignite.IgniteException;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Ignite thin client example using Java 21 features:
 * - try-with-resources for automatic client cleanup
 * - Logger instead of System.out.println
 */
public class HelloThinClient {
    private static final Logger log = LoggerFactory.getLogger(HelloThinClient.class);

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 10800;
    private static final String CACHE_NAME = "thin-cache";

    public static void main(String[] args) {
        log.info("Simple Ignite thin client example working over TCP socket.");

        var cfg = new ClientConfiguration().setAddresses(HOST + ":" + PORT);

        try (var igniteClient = Ignition.startClient(cfg)) {
            var clientCache = igniteClient.getOrCreateCache(CACHE_NAME);

            // Put a few values
            clientCache.put("Moscow", "095");
            clientCache.put("Vladimir", "033");

            // Get the region code of Vladimir
            var val = clientCache.get("Vladimir");
            log.info("Print value: {}", val);
        }
        catch (IgniteException e) {
            log.error("Ignite exception: {}", e.getMessage(), e);
        }
        catch (Exception e) {
            log.error("Unexpected error", e);
        }
    }
}
