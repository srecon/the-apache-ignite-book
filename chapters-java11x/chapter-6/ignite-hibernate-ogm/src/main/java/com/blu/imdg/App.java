package com.blu.imdg;


import com.blu.imdg.DTO.Department;
import com.blu.imdg.DTO.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
//import java.util.logging.Logger;

public class App {
    final static Logger LOGGER = LogManager.getLogger(App.class.getName());

    public static void main(String[] args) {
        LOGGER.info("-------Ignite Hibernate JPA example-------");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ogm-jpa-tutorial");

        EntityManager em =  emf.createEntityManager();

        em.getTransaction().begin();

        LOGGER.info("=======Populate DEPT table with rows======");
        Department department_10 = new Department( "ACCOUNTING", "NEW YORK");
        Department department_20 = new Department("RESEARCH", "DALLAS");
        Department department_30 = new Department( "SALES", "CHICAGO");

        // persist Departments
        em.persist(department_10);
        em.persist(department_20);
        em.persist(department_30);

        LOGGER.info("=======Populate EMP table with rows======");

        Employee emp_king = new Employee( "KING", "PRESIDENT", null, null, 5000, department_10);
        Employee emp_blake = new Employee("BLAKE", "MANAGER", 7839, null, 2850, department_30);
        Employee emp_clark = new Employee("CLARK", "MANAGER", 7839, null, 2450, department_10);
        Employee emp_jones = new Employee("JONES", "MANAGER", 7839, null, 2975, department_20);

        //persist Employees

        em.persist(emp_king);
        em.persist(emp_blake);
        em.persist(emp_clark);
        em.persist(emp_jones);

        Integer empNo = emp_king.getEmpno();

        // commit the transaction
        em.getTransaction().commit();

        // Query some employees
        LOGGER.info("=====Query by Employee ID=====");

        Employee emp = em.find(Employee.class, empNo);

        LOGGER.info("Employee KING:" + emp.getEname() + " Employee dept:" + emp.getDept().getDname());

        LOGGER.info("=====Native query example=====");

// NATIVE query is not implemented
//Caused by: java.lang.UnsupportedOperationException: complete() is not implemented
//    at org.hibernate.ogm.datastore.ignite.query.impl.IgniteSqlQueryParser$ParameterSubstitutionRecognizer.complete (IgniteSqlQueryParser.java:381)
//
//      List<Department> employees = em.createNativeQuery("Select * from DEPT;").getResultList();
//
//        LOGGER.info("Employees that gets salary more than 2000$" + employees.size());

/*
* java.lang.reflect.InvocationTargetException
    at sun.reflect.NativeMethodAccessorImpl.invoke0 (Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke (NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke (DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke (Method.java:497)
    at org.codehaus.mojo.exec.ExecJavaMojo$1.run (ExecJavaMojo.java:297)
    at java.lang.Thread.run (Thread.java:745)
Caused by: org.hibernate.ogm.exception.NotSupportedException: OGM-23 - Criteria queries are not supported yet
* */

//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaQuery<Department> query = criteriaBuilder.createQuery(Department.class);
//        Root<Department> root = query.from(Department.class);
//
//        LOGGER.info("Department Root:" + root.toString());

        // Query works!!
        LOGGER.info("=====Query example====");
        Query query = em.createQuery("Select ename from EMP e where e.empno=:empno");
        query.setParameter("empno", 1);
        List<String> employeesName = query.getResultList();
        employeesName.forEach(LOGGER::info);
        query = em.createQuery("Select ename from EMP e where e.dept=:deptno");
        query.setParameter("deptno", department_10);
        employeesName = query.getResultList();
        employeesName.forEach(LOGGER::info);
        //LOGGER.info("Group by and Having example");

//        query = em.createQuery("select avg(e.sal), d.dname from EMP e, DEPT d where e.dept = d.deptno group by d.dname having avg(e.sal) > :sal");
//        query.setParameter("sal", 2000d);
//        LOGGER.info("Result : "+query.getResultList().size());
        // return error not implemented Caused by: javax.persistence.PersistenceException: org.hibernate.cfg.NotYetImplementedException: Multiple root entities

//        LOGGER.info("Jion example");
//        query = em.createQuery("select e.ename from EMP e, DEPT d where e.dept = d.deptno and e.dept=:dept");
//        query.setParameter("dept", department_20);
//        employeesName = query.getResultList();
//
//        employeesName.forEach(LOGGER::info);
// return error not implemented Caused by: javax.persistence.PersistenceException: org.hibernate.cfg.NotYetImplementedException: Multiple root entities


        //close the entity manager
        em.close();

    }
}
