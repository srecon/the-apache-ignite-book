package com.blu.imdg;

import com.blu.imdg.dao.UserServices;
import com.blu.imdg.dto.Employee;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by shamim on 14/02/16.
 */
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
