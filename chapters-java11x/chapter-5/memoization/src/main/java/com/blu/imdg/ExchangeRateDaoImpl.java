package com.blu.imdg;

import com.blu.imdg.dao.ExchangeRateDao;
import com.blu.imdg.dto.ExchangeRate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by shamim
 */
public class ExchangeRateDaoImpl implements ExchangeRateDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Cacheable(value = "exchangeRate")
    public String getUSDollarExchangeRateByRegion(String region) {
        Session session = sessionFactory.openSession();
        // in real life, it should be current date time
        SQLQuery query = session.createSQLQuery("select * from exchangerate e where e.ratedate = TO_DATE('2015-05-02','YYYY-MM-DD') and e.region=:region");
        query.setParameter("region", region);
        query.addEntity(ExchangeRate.class);
        ExchangeRate res =  (ExchangeRate)query.uniqueResult();
        session.close();
        return String.valueOf(res.getUsdollar());
    }


    @CacheEvict(value = "exchangeRate", key = "#e.region")
    public void updateExchange(ExchangeRate e) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        SQLQuery query =  session.createSQLQuery("update exchangerate \n" +
                " set usdollar = :usdollar" +
                " where region = :region and ratedate = TO_DATE('2015-05-02','YYYY-MM-DD')") ;

        query.setParameter("region", e.getRegion());
        query.setParameter("usdollar", e.getUsdollar());
        query.addEntity(ExchangeRate.class);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
