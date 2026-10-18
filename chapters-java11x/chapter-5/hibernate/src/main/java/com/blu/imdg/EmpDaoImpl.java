package com.blu.imdg;

import com.blu.imdg.dao.EmpDao;
import com.blu.imdg.dto.Employee;
import com.blu.imdg.dto.ExchangeRate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by shamim
 */
public class EmpDaoImpl implements EmpDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Employee> getAllEmployees() {
        Session session = sessionFactory.openSession();
        Query<Employee> query = session.createQuery("from Employee e", Employee.class);
        List<Employee> employees =  query.list();
        session.close();
        if(employees != null && !employees.isEmpty()){
            return employees;
        }
        return Collections.emptyList();
    }

    public Employee getEmployeeById(int empno) {
        Session session = sessionFactory.openSession();
        Employee emp = session.get(Employee.class, empno);

        session.close();

        return emp;
    }

    public void create(int empno, String ename, String job, Integer mgr) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        Employee emp = new Employee();
        emp.setEmpno(empno);
        emp.setEname(ename);
        emp.setJob(job);
        emp.setMgr(mgr);
        emp.setSal(1111);
        emp.setDeptno(10);
        emp.setHireDate(LocalDate.now());
        emp.setComm(111);
        session.save(emp);
        session.getTransaction().commit();
        session.close();
    }

    public List<Employee> getEmpByName(String ename) {
        Session session = sessionFactory.openSession();
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = cb.createQuery(Employee.class);
        var root = criteriaQuery.from(Employee.class);
        criteriaQuery.where(cb.like(root.get("ename"), ename.replace('*', '%'), '\\'));
        List<Employee> employees = session.createQuery(criteriaQuery).list();
        session.close();
        return employees;
    }


    //@Cacheable(value = "exchangeRate")
    public String getExchangeRateByRegion(String region) {
        Session session = sessionFactory.openSession();
        // in real life, it should be current date time
        NativeQuery<ExchangeRate> query = session.createNativeQuery("select * from exchangerate e where e.ratedate = TO_DATE('2015-05-02','YYYY-MM-DD') and e.region=:region", ExchangeRate.class);
        query.setParameter("region", region);
        ExchangeRate res =  (ExchangeRate)query.uniqueResult();
        session.close();
        return String.valueOf(res.getUsdollar());
    }


    //@CacheEvict(value = "exchangeRate", key = "#e.region")
    public void updateExchange(ExchangeRate e) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        NativeQuery<ExchangeRate> query =  session.createNativeQuery("update exchangerate \n" +
                " set usdollar = :usdollar" +
                " where region = :region and ratedate = TO_DATE('2015-05-02','YYYY-MM-DD')") ;

        query.setParameter("region", e.getRegion());
        query.setParameter("usdollar", e.getUsdollar());
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
