package com.blu.imdg.dao;

import com.blu.imdg.dto.Employee;
import com.blu.imdg.dto.ExchangeRate;

import java.util.List;

/**
 * Created by shamim
 */
public interface EmpDao {
    List<Employee> getAllEmployees ();
    Employee getEmployeeById (int empno);
    void create(Integer empno, String ename, String job, Integer mgr );
    List<Employee> getEmpByName(String ename);
    String getExchangeRateByRegion(String region);
    void updateExchange(ExchangeRate e);
}
