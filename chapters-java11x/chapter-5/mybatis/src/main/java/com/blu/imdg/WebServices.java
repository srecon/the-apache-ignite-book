package com.blu.imdg;

import com.blu.imdg.dao.UserServices;
import com.blu.imdg.dto.Employee;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(name = "BusinessRulesServices",
        serviceName="BusinessRulesServices",
        targetNamespace = "http://com.blu.rules/services")
public class WebServices {
    private UserServices userServices;

    @WebMethod(exclude = true)
    public void setDao(UserServices userServices){
        this.userServices = userServices;
    }

    @WebMethod(operationName = "getEmployee")
    public Employee getEmployee (@WebParam(name = "EmployeeName") String eName) {return userServices.getEmployee(eName);}

}
