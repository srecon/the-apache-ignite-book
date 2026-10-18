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

import com.blu.imdg.dao.ExchangeRateDao;
import com.blu.imdg.dto.ExchangeRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDate;

/**
 * Entry point for Hibernate memoization demo.
 */
public class Run {
    private static final Logger log = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) {
        log.info("Hello Hibernate after a long time!!");
        var ctx = new ClassPathXmlApplicationContext("spring-core.xml");
        var exchangeRateDao = ctx.getBean(ExchangeRateDao.class);

        // Update exchange rate example
        var e = new ExchangeRate(
            "Moscow", LocalDate.now(), 71.17, 68.50, 65.30
        );
        exchangeRateDao.updateExchange(e);
        log.info("Return size: {}", exchangeRateDao.getUSDollarExchangeRateByRegion("Moscow"));
    }
}
