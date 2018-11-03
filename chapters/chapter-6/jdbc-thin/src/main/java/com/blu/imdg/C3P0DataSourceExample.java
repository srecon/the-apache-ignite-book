package com.blu.imdg;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class C3P0DataSourceExample {
    private static ComboPooledDataSource cpds;
    private static final String JDBC_URL = "jdbc:ignite:thin://127.0.0.1";

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

    public static void main(String[] args) throws SQLException{
        System.out.println("Data source example!!");

        if(cpds!= null){
            Connection conn = cpds.getConnection();
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
