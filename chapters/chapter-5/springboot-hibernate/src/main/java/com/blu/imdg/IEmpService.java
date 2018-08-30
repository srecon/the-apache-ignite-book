package com.blu.imdg;

import com.blu.imdg.dto.Employee;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IEmpService {
    List<Employee> getEmpByName(String ename);
}
