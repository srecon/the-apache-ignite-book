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
import com.blu.imdg.dto.ServiceStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.List;


/** Queries service status for 404 errors. */
public class QueryStatus {
    private static final Logger log = LogManager.getLogger(QueryStatus.class);

    private static final String QUERY_404 = """
        SELECT serviceName, statusCode from ServiceStatus where statusCode = 404
        """;

    private static final String QUERY_CNT = """
        select count(*) from ServiceStatus where statusCode = 404
        """;

    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (var ignite = Ignition.start("example-ignite.xml")) {
            if (!ExamplesUtils.hasServerNodes(ignite))
                return;

            // Query code goes here.
            var healthchecksCfg = new CacheConfiguration<String, ServiceStatus>("healthchecks");
            var instCache = ignite.getOrCreateCache(healthchecksCfg);
            var query = new SqlFieldsQuery(QUERY_404);

            while (true) {
                // Execute queries.
                var res = instCache.query(query).getAll();

                log.info("Service Health check status");

                ExamplesUtils.printQueryResults(res);

                Thread.sleep(1000);
            }
        }
    }
}
