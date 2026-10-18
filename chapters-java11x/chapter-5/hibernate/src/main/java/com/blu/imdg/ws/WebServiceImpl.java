/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blu.imdg.ws;

import com.blu.imdg.dao.EmpDao;
import com.blu.imdg.dto.Employee;
import com.blu.imdg.dto.ExchangeRate;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.time.LocalDate;
import java.util.List;

/**
 * JAX-WS web service with Jakarta annotations.
 */
@WebService(name = "BusinessRulesServices", serviceName = "BusinessRulesServices",
        targetNamespace = "http://com.blu.rules/services")
public class WebServiceImpl {
    private EmpDao empDao;

    @WebMethod(exclude = true)
    public void setEmpDao(EmpDao empDao) {
        this.empDao = empDao;
    }

    @WebMethod(operationName = "getAllEmployees")
    public List<Employee> getAllEmployees() {
        return empDao.getAllEmployees();
    }

    @WebMethod(operationName = "addEmployee")
    public void addEmployee(@WebParam(name = "Employee_ID") int empno,
                            @WebParam(name = "Name") String ename,
                            @WebParam(name = "Job_title") String job,
                            @WebParam(name = "Manager_ID") Integer mgr) {
        empDao.create(empno, ename, job, mgr);
    }

    @WebMethod(operationName = "getEmpByName")
    public List<Employee> getEmpByName(@WebParam(name = "Employee_Name") String ename) {
        return empDao.getEmpByName(ename);
    }

    @WebMethod(operationName = "getEmpByNo")
    public Employee getEmpByNo(@WebParam(name = "Employee_ID") int empno) {
        return empDao.getEmployeeById(empno);
    }

    @WebMethod(operationName = "getExchangeRateByRegion")
    public String getExchangeRateByRegion(String region) {
        return empDao.getExchangeRateByRegion(region);
    }

    @WebMethod(operationName = "updateExchangeRate")
    public void updateExchangeRate(@WebParam(name = "Region") String region,
                                   @WebParam(name = "Date") LocalDate rateDate,
                                   @WebParam(name = "USdollarRate") double usdollar,
                                   @WebParam(name = "EuroRate") double euro,
                                   @WebParam(name = "BritishPoundRate") double gbp) {
        var e = new ExchangeRate(region, rateDate, usdollar, euro, gbp);
        empDao.updateExchange(e);
    }
}
