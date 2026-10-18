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

import com.blu.dto.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import java.util.Map;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spring-integrated Ignite example with Java 21 features.
 */
public class HelloIgniteSpring {
    private static final Logger log = LoggerFactory.getLogger(HelloIgniteSpring.class);

    public static void main(String[] args) {
        // Start Ignite cluster
        try (var ignite = Ignition.start("default-config.xml")) {
            var cache = ignite.getOrCreateCache("testCache");

            var p1 = new Person(37, "Shamim");
            var p2 = new Person(2, "Mishel");
            var p3 = new Person(55, "Scott");
            var p4 = new Person(5, "Tiger");

            cache.putAll(Map.of(1, p1, 2, p2, 3, p3, 4, p4));

            log.info("Cached persons. Enter Ctrl+C to exit the application.");
        }
    }
}
