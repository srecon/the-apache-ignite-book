package com.blu.imdg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class JDBCThinClientExample {
    private static final String JDBC_URL = "jdbc:ignite:thin://127.0.0.1";

    public static void main(String[] args) throws Exception{
        System.out.println("JDBC thin client example!!");
        // Register JDBC driver.
        Class.forName("org.apache.ignite.IgniteJdbcThinDriver");

        Connection conn = DriverManager.getConnection(JDBC_URL);

        // query some data
        ResultSet rs = conn.createStatement().executeQuery("SELECT e.ename, e.empno, e.deptno, d.loc FROM emp e, dept d\n" +
                "WHERE e.deptno = d.deptno\n" +
                "ORDER BY e.ename;");
        while (rs.next()){
            String ename = rs.getString(1);
            System.out.println("Employee name:" + ename);
        }

        // close connection
        conn.close();
    }
}
