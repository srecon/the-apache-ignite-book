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
import org.apache.camel.Exchange;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/** Demonstrates direct file ingestion into Ignite via Camel streamer. */
public class CamelStreamerDirectIngestion {
    private static final Logger log = LogManager.getLogger(CamelStreamerDirectIngestion.class);

    private static final String FILE_PROPERTIES = "camel.properties";

    public static void main(String[] args) {
        log.info("Camel Streamer Direct ingestion!");

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
        var camelCacheCfg = new CacheConfiguration<String, MnpRouting>("camel-direct");
        camelCacheCfg.setIndexedTypes(String.class, MnpRouting.class);

        var camelCache = ignite.getOrCreateCache(camelCacheCfg);

        // Create a streamer pipe which ingests into the 'camel_direct' cache.
        var pipe = ignite.dataStreamer(camelCache.getName());
        pipe.autoFlushFrequency(1L);
        pipe.allowOverwrite(true);

        // Create a Camel streamer and connect it.
        var streamer = new CamelStreamer<String, MnpRouting>();
        streamer.setIgnite(ignite);
        streamer.setStreamer(pipe);

        streamer.setEndpointUri("file://" + fileLocation);
        streamer.setSingleTupleExtractor(new StreamSingleTupleExtractor<Exchange, String, MnpRouting>() {
            @Override
            public java.util.Map.Entry<String, MnpRouting> extract(Exchange exchange) {
                var mapper = new ObjectMapper();
                var msgBody = exchange.getIn().getBody(String.class);
                log.info("MsgBody: {}", msgBody);

                try (var is = new java.io.ByteArrayInputStream(msgBody.getBytes())) {
                    var obj = mapper.readValue(is, MnpRouting.class);
                    if (obj != null) {
                        return new GridMapEntry<>(obj.telephone(), obj);
                    }
                } catch (IOException e) {
                    log.error("Failed to parse MNP routing message: {}", e.getMessage());
                }

                return new GridMapEntry<>(null, null);
            }
        });

        // Start the streamer.
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
