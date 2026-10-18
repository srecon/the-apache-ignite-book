package com.blu.imdg;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HikariCPDataSourceExample {

    private static final Logger log = LoggerFactory.getLogger(HikariCPDataSourceExample.class);

    private static final String JDBC_URL = "jdbc:ignite:thin://127.0.0.1";

    private static final String SQL_SALARY_MORE_THAN_3000 = """
        SELECT ename, deptno, job, sal
        FROM EMP
        WHERE (1.25 * sal) > 3000""";

    private static final String SQL_SALARY_SMALLEST = """
        SELECT ename, deptno, sal
        FROM emp
        WHERE sal IN (
            SELECT MIN(sal)
            FROM emp
            GROUP BY deptno
        )""";

    private static final String SQL_SALARY_MORE_THAN_AVG = """
        SELECT ename, deptno, sal
        FROM emp
        WHERE sal > (SELECT AVG(sal) FROM emp)""";

    private static final HikariDataSource hds = createDataSource();

    private static HikariDataSource createDataSource() {
        var ds = new HikariDataSource();
        ds.setJdbcUrl(JDBC_URL);
        ds.setDriverClassName("org.apache.ignite.IgniteJdbcThinDriver");
        return ds;
    }

    public static void main(String[] args) {
        log.info("HikariCP data source example!");

        if (hds == null) {
            log.warn("Hikari data source is not available");
            return;
        }

        try (var conn = hds.getConnection()) {
            log.info("A list of the employees whose salary is more than 3000 after giving 25%!");
            try (var rs = conn.createStatement().executeQuery(SQL_SALARY_MORE_THAN_3000)) {
                while (rs.next()) {
                    log.info("Employee name: {} Department: {} Job: {} Salary: {}",
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                }
            }

            log.info("A list of employees who earn such amount of salary which is the smallest salary of any of the departments!");
            try (var rs = conn.createStatement().executeQuery(SQL_SALARY_SMALLEST)) {
                while (rs.next()) {
                    log.info("Employee name: {} Department: {} Salary: {}",
                        rs.getString(1), rs.getString(2), rs.getString(3));
                }
            }

            log.info("A list of employees who earn more than the average salary!");
            try (var rs = conn.createStatement().executeQuery(SQL_SALARY_MORE_THAN_AVG)) {
                while (rs.next()) {
                    log.info("Employee name: {} Department: {} Salary: {}",
                        rs.getString(1), rs.getString(2), rs.getString(3));
                }
            }
        } catch (SQLException e) {
            log.error("SQL error", e);
        }
    }
}
