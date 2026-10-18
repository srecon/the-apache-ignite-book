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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exchange rate DAO with Hibernate session management and Spring cache integration.
 */
public class ExchangeRateDaoImpl implements ExchangeRateDao {
    private static final Logger log = LoggerFactory.getLogger(ExchangeRateDaoImpl.class);
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Cacheable(value = "exchangeRate")
    @Override
    public String getUSDollarExchangeRateByRegion(String region) {
        var session = sessionFactory.openSession();
        try {
            NativeQuery<ExchangeRate> query = session.createNativeQuery(
                "SELECT e.usdollar FROM exchangerate e " +
                "WHERE e.ratedate = DATE '2015-05-02' AND e.region = :region",
                ExchangeRate.class
            );
            query.setParameter("region", region);
            var res = query.uniqueResult();
            return res != null ? String.valueOf(res.getUsdollar()) : "N/A";
        }
        finally {
            session.close();
        }
    }

    @CacheEvict(value = "exchangeRate", key = "#e.region")
    @Override
    public void updateExchange(ExchangeRate e) {
        var session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            NativeQuery<Object> query = session.createNativeQuery("""
                UPDATE exchangerate
                SET usdollar = :usdollar
                WHERE region = :region AND ratedate = DATE '2015-05-02'
                """);
            query.setParameter("region", e.getRegion());
            query.setParameter("usdollar", e.getUsdollar());
            query.executeUpdate();
            session.getTransaction().commit();
        }
        finally {
            session.close();
        }
    }
}
