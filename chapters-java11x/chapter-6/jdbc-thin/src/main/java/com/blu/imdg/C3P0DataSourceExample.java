package com.blu.imdg;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class C3P0DataSourceExample {
    private static ComboPooledDataSource cpds;

    private static final String JDBC_URL = "jdbc:ignite:thin://127.0.0.1";
    private static final String SQL_SALARY_MORE_THAN_3000 = "SELECT ename, deptno, job, sal\n" +
            "FROM EMP\n" +
            "WHERE (1.25 * sal) > 3000 ;";
    private static final String SQL_SALARY_SMALLEST="SELECT ename, deptno, sal  \n" +
            "FROM emp  \n" +
            "WHERE sal IN  \n" +
            "( SELECT MIN(sal)  \n" +
            "FROM emp  \n" +
            "GROUP BY deptno \n" +
            ");";
    private static final String SQL_SALARY_MORE_THAN_AVG="SELECT ename, deptno, sal  \n" +
            "FROM emp  \n" +
            "WHERE sal >  \n" +
            "( SELECT AVG(sal)  \n" +
            "FROM emp \n" +
            ");";

    static {
        cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass("org.apache.ignite.IgniteJdbcThinDriver");
            cpds.setJdbcUrl(JDBC_URL);

            // pool size setting (optional), c3p0 can work with defaults
            cpds.setMinPoolSize(5);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(20);
            cpds.setMaxStatements(180);

        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Data source example!!");

        if(cpds!= null){

            try(Connection conn = cpds.getConnection()){
                ResultSet rs = conn.createStatement().executeQuery(SQL_SALARY_MORE_THAN_3000);

                System.out.println("A list of the employees whose salary is more than 3000 after giving 25% !!!");
                while (rs.next()){
                    System.out.println("Employee name: " + rs.getString(1) + " Department: "+rs.getString(2) + " Job:" + rs.getString(3) + " Salary: " + rs.getString(4));
                }

                //Execute an query to display the name ( first name and last name ), salary, department id for those employees who earn such amount of salary which is the smallest salary of any of the departments.
                rs = conn.createStatement().executeQuery(SQL_SALARY_SMALLEST);

                System.out.println("A list of employees who earn such amount of salary which is the smallest salary of any of the departments!!!");
                while (rs.next()){
                    System.out.println("Employee name: " + rs.getString(1) + " Department: "+rs.getString(2) + " Salary: " + rs.getString(3));
                }

                //Execute an query query to display the employee id, employee name (first name and last name ) for all employees who earn more than the average salary.
                rs = conn.createStatement().executeQuery(SQL_SALARY_MORE_THAN_AVG);

                System.out.println("A list of employees who earn more than the average salary!!!");
                while (rs.next()){
                    System.out.println("Employee name: " + rs.getString(1) + " Department: "+rs.getString(2) + " Salary: " + rs.getString(3));
                }

            } catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
