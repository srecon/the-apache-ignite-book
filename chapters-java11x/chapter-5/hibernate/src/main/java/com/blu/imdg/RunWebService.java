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

import com.blu.imdg.ws.WebService;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jakarta.xml.ws.Endpoint;

/**
 * Application entry point that publishes the WebService as a JAX-WS endpoint.
 */
public class RunWebService {
    private static final Logger log = LoggerFactory.getLogger(RunWebService.class);

    public static void main(String[] args) {
        try (var ignite = Ignition.start("ignite-l2-config.xml")) {
            var ctx = new ClassPathXmlApplicationContext("spring-core.xml");

            var service = ctx.getBean(WebService.class);

            Endpoint.publish("http://localhost:7001/invokeRules", service);
            log.info("Server started on port 7001");
            log.info("Web service WSDL available at http://localhost:7001/invokeRules?wsdl");
        }
    }
}
