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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.List;


/** Utility methods shared across examples. */
public class ExamplesUtils {
    private static final Logger log = LogManager.getLogger(ExamplesUtils.class);

    private static final ClassLoader CLS_LDR = ExamplesUtils.class.getClassLoader();

    /**
     * Exits with code {@code -1} if maximum memory is below 85% of minimally allowed threshold.
     *
     * @param min Minimum memory threshold in bytes.
     */
    public static void checkMinMemory(long min) {
        long maxMem = Runtime.getRuntime().maxMemory();

        if (maxMem < .85 * min) {
            var heapMb = maxMem / (1024 * 1024);
            var minMb = min / (1024 * 1024);
            log.error("Heap limit is too low ({}MB), please increase heap size at least up to {}MB.", heapMb, minMb);

            System.exit(-1);
        }
    }

    /**
     * Returns URL resolved by class loader for classes in examples project.
     *
     * @param path Resource path.
     * @return Resolved URL.
     */
    public static URL url(String path) {
        var url = CLS_LDR.getResource(path);

        if (url == null)
            throw new RuntimeException("Failed to resolve resource URL by path: " + path);

        return url;
    }

    /**
     * Checks minimum topology size for running a certain example.
     *
     * @param grp Cluster to check size for.
     * @param size  Minimum number of nodes required.
     * @return {@code true} if check passed, {@code false} otherwise.
     */
    public static boolean checkMinTopologySize(org.apache.ignite.cluster.ClusterGroup grp, int size) {
        var prjSize = grp.nodes().size();

        if (prjSize < size) {
            log.warn(">>> Please start at least {} cluster nodes to run example.", size);

            return false;
        }

        return true;
    }

    /**
     * Checks if cluster has server nodes.
     *
     * @param ignite Ignite instance.
     * @return {@code true} if cluster has server nodes, {@code false} otherwise.
     */
    public static boolean hasServerNodes(org.apache.ignite.Ignite ignite) {
        if (ignite.cluster().forServers().nodes().isEmpty()) {
            log.error("Server nodes not found (start data nodes with ExampleNodeStartup class)");

            return false;
        }

        return true;
    }

    /**
     * Convenience method for printing query results.
     *
     * @param res Query results.
     */
    public static void printQueryResults(List<?> res) {
        if (res == null || res.isEmpty()) {
            log.info("Query result set is empty.");
            return;
        }

        for (var row : res) {
            if (row instanceof List<?> l) {
                var sb = new StringBuilder("(");

                for (var i = 0; i < l.size(); i++) {
                    var o = l.get(i);

                    if (o instanceof Double d)
                        sb.append("%,.2f".formatted(d));
                    else if (o instanceof Float f)
                        sb.append("%,.2f".formatted(f.doubleValue()));
                    else
                        sb.append(o);

                    if (i + 1 != l.size())
                        sb.append(',');
                }

                sb.append(')');
                log.info("{}", sb);
            }
            else {
                log.info("  {}", row);
            }
        }
    }
}
