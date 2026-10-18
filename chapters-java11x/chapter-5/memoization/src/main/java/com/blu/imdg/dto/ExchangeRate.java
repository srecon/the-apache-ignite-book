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

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Exchange rate entity with Jakarta persistence annotations.
 */
@Entity
@Table(name = "exchangerate")
public class ExchangeRate {
    @Id
    @Column(name = "region")
    private String region;

    @Column(name = "ratedate")
    private LocalDate rateDate;

    @Column(name = "usdollar")
    private double usdollar;

    @Column(name = "euro")
    private double euro;

    @Column(name = "gbp")
    private double gbp;

    public ExchangeRate() {
    }

    public ExchangeRate(String region, LocalDate rateDate, double usdollar, double euro, double gbp) {
        this.region = region;
        this.rateDate = rateDate;
        this.usdollar = usdollar;
        this.euro = euro;
        this.gbp = gbp;
    }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public LocalDate getRateDate() { return rateDate; }
    public void setRateDate(LocalDate rateDate) { this.rateDate = rateDate; }
    public double getUsdollar() { return usdollar; }
    public void setUsdollar(double usdollar) { this.usdollar = usdollar; }
    public double getEuro() { return euro; }
    public void setEuro(double euro) { this.euro = euro; }
    public double getGbp() { return gbp; }
    public void setGbp(double gbp) { this.gbp = gbp; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate that = (ExchangeRate) o;
        return region.equals(that.region) && rateDate.equals(that.rateDate);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(region, rateDate);
    }
}
