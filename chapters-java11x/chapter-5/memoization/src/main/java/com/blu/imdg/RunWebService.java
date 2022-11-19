package com.blu.imdg;

import com.blu.imdg.ws.WebService;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.ws.Endpoint;


/**
 * Created by shamim
 */
public class RunWebService {
    public static void main(String[] args) {

//        try(Ignite ignite = Ignition.start("ignite-l2-config.xml")){
//            ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-core.xml");
//
//            WebService service = (WebService) ctx.getBean("serviceBean");
//
//            Endpoint.publish("http://localhost:7001/invokeRules", service);
//            System.out.println("Server start in Port .. 7001");
//            System.out.println("Web service WSDL is available at http://localhost:7001/invokeRules?wsdl");
//        }
        Ignite ignite = Ignition.start("ignite-l2-config.xml");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-core.xml");

        WebService service = (WebService) ctx.getBean("serviceBean");

        Endpoint.publish("http://localhost:7001/invokeRules", service);
        System.out.println("Server start in Port .. 7001");
        System.out.println("Web service WSDL is available at http://localhost:7001/invokeRules?wsdl");

    }
}

