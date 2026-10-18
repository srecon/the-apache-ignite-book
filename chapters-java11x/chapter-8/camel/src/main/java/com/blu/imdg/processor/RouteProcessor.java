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

package com.blu.imdg.processor;

import com.blu.imdg.dto.MnpRouting;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/** Validates and processes MNP routing messages. */
public class RouteProcessor implements Processor {
    private static final Logger log = LogManager.getLogger(RouteProcessor.class);

    private static final long CREDIT_LIMIT = 100L;

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Processing MNP routing message");

        var body = exchange.getIn().getBody(MnpRouting.class);

        if (body != null) {
            // Validate phone numbers of format "1234567890" and credit limit
            if (!body.telephone().matches("\\d{11}") || body.credit() < CREDIT_LIMIT) {
                log.info("Validation failed for telephone: {}", body.telephone());
                exchange.getOut().setBody("Message doesn't pass validation");
                exchange.getOut().setHeader("key", body.telephone());
            } else {
                exchange.getOut().setBody(body.toString());
                exchange.getOut().setHeader("key", body.telephone());
            }
        }
    }
}
