package com.blu.imdg;

import com.blu.imdg.dao.EmployeeDAO;
import com.blu.imdg.dto.Employee;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

@Transactional
@Repository
public class EmployeeDAOImpl implements EmployeeDAO{

    //@PersistenceContext
    //private EntityManager entityManager;

    @Override
    public List<Employee> getAllEmployees() {
        return null;
    }

    @Override
    public void create(Integer empno, String ename, String job, Integer mgr) { }

    @Override
    public List<Employee> getEmpByName(String ename) {
        System.out.println("input:" + ename);

        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("postgres");
        final EntityManager em = emf.createEntityManager();

        final String SQL = "from Employee e where e.ename=:ename";

        List<Employee> employees= em.createQuery(SQL).setParameter("ename", ename).getResultList();

        em.close();
        emf.close();

        return employees;
    }
}
