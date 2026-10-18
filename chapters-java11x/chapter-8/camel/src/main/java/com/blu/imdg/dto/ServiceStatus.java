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

package com.blu.imdg.dto;

import org.apache.ignite.cache.query.annotations.QuerySqlField;


/** Immutable service status DTO with SQL query support. */
public record ServiceStatus(
    @QuerySqlField(index = true) String serviceName,
    @QuerySqlField(index = true) int statusCode,
    @QuerySqlField(index = true) long responseTime,
    @QuerySqlField(index = true) long unavailableCnt,
    @QuerySqlField(index = true) long healthCheckCnt
) {
    /** Convenience constructor with default values for remaining fields. */
    public ServiceStatus(String name, int statusCode) {
        this(name, statusCode, 0L, 0L, 0L);
    }

    @Override
    public String toString() {
        return "ServiceStatus{serviceName='%s', statusCode=%d, responseTime=%d, unavailableCnt=%d, healthCheckCnt=%d}"
            .formatted(serviceName, statusCode, responseTime, unavailableCnt, healthCheckCnt);
    }
}
