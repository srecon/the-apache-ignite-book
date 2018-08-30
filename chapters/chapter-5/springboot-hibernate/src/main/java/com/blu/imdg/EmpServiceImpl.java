package com.blu.imdg;

import com.blu.imdg.dao.EmployeeDAO;
import com.blu.imdg.dto.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpServiceImpl implements IEmpService{

    @Autowired
    private EmployeeDAO employeeDAO;
    @Override
    public List<Employee> getEmpByName(String ename) {

        return employeeDAO.getEmpByName(ename);
    }
}
