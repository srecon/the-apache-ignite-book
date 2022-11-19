package com.blu.imdg.dao;

import com.blu.imdg.dto.Employee;

import java.util.List;

public interface EmployeeDAO {
    List<Employee> getAllEmployees ();
    void create(Integer empno, String ename, String job, Integer mgr);
    List<Employee> getEmpByName(String ename);

}
