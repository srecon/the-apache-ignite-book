package com.blu.imdg;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HikariCPDataSourceExample {
    private static HikariDataSource hds;

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

    static{
        hds = new HikariDataSource();
        hds.setJdbcUrl(JDBC_URL);
        hds.setDriverClassName("org.apache.ignite.IgniteJdbcThinDriver");
    }

    public static void main(String[] args) {
        System.out.println("HikariDatasource example!");

        if(hds != null){
            try(Connection conn = hds.getConnection()){
                // Execute an query to list the employees whose salary is more than 3000 after giving 25%
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
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
