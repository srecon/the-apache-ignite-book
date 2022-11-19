package com.blu.imdg.ws;

import com.blu.imdg.dao.ExchangeRateDao;
import com.blu.imdg.dto.ExchangeRate;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.util.Date;

/**
 * Created by shamim
 */
@javax.jws.WebService(name = "BusinessRulesServices",
        serviceName = "BusinessRulesServices",
        targetNamespace = "http://com.blu.rules/services")
public class WebService {
    private ExchangeRateDao exchangeRateDao;

    @WebMethod(exclude = true)
    public void setEmpDao(ExchangeRateDao empDao) {
        this.exchangeRateDao = empDao;
    }

    @WebMethod(operationName = "getExchangeRateByRegion")
    public String getExchangeRateByRegion(@WebParam(name = "RegionName") String str) {
        return exchangeRateDao.getUSDollarExchangeRateByRegion(str);
    }

    @WebMethod(operationName = "updateExchangeRate")
    public void updateExchangeRate(@WebParam(name = "RegionName") String region, @WebParam(name = "Date")Date rateDate, @WebParam(name = "USdollarRate") double usdollar, @WebParam(name = "EuroRate") double euro, @WebParam(name = "BritishPoundRate") double gbp) {
        ExchangeRate e = new ExchangeRate();
        e.setRegion(region);
        e.setUsdollar(usdollar);
        e.setEuro(euro);
        e.setGbp(gbp);
        e.setRateDate(rateDate);

        exchangeRateDao.updateExchange(e);
    }
}