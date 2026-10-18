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


/** Demonstrates Camel streamer mediation with Jetty endpoint and Jackson deserialization. */
public class CamelStreamerMediation {
    private static final Logger log = LogManager.getLogger(CamelStreamerMediation.class);

    public static void main(String[] args) throws Exception {
        log.info("Camel Streamer Mediation ingestion!");

        Ignition.setClientMode(true);
        var ignite = Ignition.start("example-ignite.xml");

        // Create a CamelContext with a custom route that will:
        //  (1) consume from our Jetty endpoint.
        //  (2) transform incoming JSON into a Java object with Jackson.
        //  (3) uses JSR 303 Bean Validation to validate the object.
        //  (4) dispatches to the direct:ignite.ingest endpoint, where the streamer is consuming from.

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
                from("jetty:http://127.0.0.1:8081/ignite?httpMethodRestrict=POST")
                    .unmarshal().json(JsonLibrary.Jackson)
                    .to("bean-validator:validate")
                    .to("direct:ignite.ingest");
            }
        });

        // Remember our Streamer is now consuming from the Direct endpoint above.
        streamer.setCamelContext(context);
        streamer.setSingleTupleExtractor(new StreamSingleTupleExtractor<Exchange, String, String>() {
            @Override
            public java.util.Map.Entry<String, String> extract(Exchange exchange) {
                var stationId = exchange.getIn().getHeader("X-StationId", String.class);
                var temperature = exchange.getIn().getBody(String.class);
                log.info("StationId: {} temperature: {}", stationId, temperature);

                return new GridMapEntry<>(stationId, temperature);
            }
        });

        streamer.start();
    }
}
