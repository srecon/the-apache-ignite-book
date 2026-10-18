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

package com.blu.imdg.query;

import com.blu.imdg.ExamplesUtils;
import com.blu.imdg.dto.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.List;


/** Monitors alert levels via SQL queries on the alerts cache. */
public class AlertMonitoring {
    private static final Logger log = LogManager.getLogger(AlertMonitoring.class);

    private static final String QUERY_RED = """
        select alertLevel, count(alertLevel) from Alert group by alertLevel
        """;

    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (var ignite = Ignition.start("example-ignite.xml")) {
            if (!ExamplesUtils.hasServerNodes(ignite))
                return;

            var alertCfg = new CacheConfiguration<String, Alert>("alerts");
            var instCache = ignite.getOrCreateCache(alertCfg);

            var top3qry = new SqlFieldsQuery(QUERY_RED);

            while (true) {
                // Execute queries.
                var top3 = instCache.query(top3qry).getAll();

                log.info("Service Health Monitoring");

                ExamplesUtils.printQueryResults(top3);

                Thread.sleep(1000);
            }
        }
    }
}
