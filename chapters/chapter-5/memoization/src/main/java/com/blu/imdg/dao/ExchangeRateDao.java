package com.blu.imdg.dao;


import com.blu.imdg.dto.ExchangeRate;

/**
 * Created by shamim
 */
public interface ExchangeRateDao {
    String getUSDollarExchangeRateByRegion(String region);
    void updateExchange(ExchangeRate e);
}
