package com.blu.imdg;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HikariCPDataSourceExample {
    private static HikariDataSource hds;
    private static final String JDBC_URL = "jdbc:ignite:thin://127.0.0.1";

    static{
        hds = new HikariDataSource();
        hds.setJdbcUrl(JDBC_URL);
        hds.setDriverClassName("org.apache.ignite.IgniteJdbcThinDriver");
    }

    public static void main(String[] args) throws SQLException{
        System.out.println("HikariDatasource example!");

        if(hds != null){
            Connection conn = hds.getConnection();
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
}
