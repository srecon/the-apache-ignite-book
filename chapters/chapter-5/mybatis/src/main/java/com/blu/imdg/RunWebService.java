package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.ws.Endpoint;


public class RunWebService {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-core.xml");
        WebServices services = (WebServices) ctx.getBean("servicesBean");

        Endpoint.publish("http://localhost:7001/invokeRules", services);
        System.out.println("Server start in Port .. 7001");
        System.out.println("Web service WSDL is available at http://localhost:7001/invokeRules?wsdl");
    }
}
