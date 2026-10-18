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

package com.blu.imdg.camel;

import com.blu.imdg.ExamplesUtils;
import com.blu.imdg.dto.MnpRouting;
import com.blu.imdg.processor.RouteProcessor;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.internal.util.lang.GridMapEntry;
import org.apache.ignite.stream.StreamSingleTupleExtractor;
import org.apache.ignite.stream.camel.CamelStreamer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/** Demonstrates Camel streamer mediation with file source and route processing. */
public class CamelStreamerMediationIngestion {
    private static final Logger log = LogManager.getLogger(CamelStreamerMediationIngestion.class);

    private static final String FILE_PROPERTIES = "camel.properties";

    public static void main(String[] args) throws Exception {
        log.info("Camel Streamer Mediation ingestion!");

        Ignition.setClientMode(true);
        var ignite = Ignition.start("example-ignite.xml");

        if (!ExamplesUtils.hasServerNodes(ignite))
            return;

        var fileLocation = getFileLocation();
        if (fileLocation == null || fileLocation.isEmpty()) {
            log.error("Properties file is empty or null!");
            return;
        }

        // Cache configuration
        var camelCacheCfg = new CacheConfiguration<String, String>("camel-direct");
        camelCacheCfg.setIndexedTypes(String.class, String.class);

        var camelCache = ignite.getOrCreateCache(camelCacheCfg);

        // Create a streamer pipe which ingests into the 'camel_direct' cache.
        var pipe = ignite.dataStreamer(camelCache.getName());
        pipe.autoFlushFrequency(1L);
        pipe.allowOverwrite(true);

        // Create a Camel streamer and connect it.
        var streamer = new CamelStreamer<String, String>();
        streamer.setIgnite(ignite);
        streamer.setStreamer(pipe);
        streamer.setEndpointUri("direct:ignite.ingest");

        var context = new DefaultCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file://" + fileLocation)
                    .unmarshal().json(JsonLibrary.Jackson, MnpRouting.class)
                    .bean(new RouteProcessor(), "process")
                    .to("direct:ignite.ingest");
            }
        });

        streamer.setCamelContext(context);
        streamer.setSingleTupleExtractor(new StreamSingleTupleExtractor<Exchange, String, String>() {
            @Override
            public java.util.Map.Entry<String, String> extract(Exchange exchange) {
                var key = exchange.getIn().getHeader("key", String.class);
                var routeMsg = exchange.getIn().getBody(String.class);

                return new GridMapEntry<>(key, routeMsg);
            }
        });

        streamer.start();
    }

    private static String getFileLocation() {
        try (var in = CamelStreamerDirectIngestion.class.getClassLoader().getResourceAsStream(FILE_PROPERTIES)) {
            if (in == null)
                return null;

            var properties = new Properties();
            properties.load(in);
            return properties.getProperty("input.file.location");
        } catch (IOException e) {
            log.error("Failed to load camel.properties: {}", e.getMessage());
            return null;
        }
    }
}
