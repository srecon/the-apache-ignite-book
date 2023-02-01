package com.blu.imdg;

import com.blu.imdg.ws.WebService;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.ws.Endpoint;


/**
 * Created by shamim
 * java --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED -jar ./target/hibernate-1.0-SNAPSHOT-spring-boot.jar
 */
public class RunWebService {
    public static void main(String[] args) {
        //Ignite ignite = Ignition.start("ignite-client-config.xml");
        Ignite ignite = Ignition.start("ignite-l2-config.xml");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-core.xml");

        WebService service = (WebService) ctx.getBean("serviceBean");

        Endpoint.publish("http://localhost:7001/invokeRules", service);
        System.out.println("Server start in Port .. 7001");
        System.out.println("Web service WSDL is available at http://localhost:7001/invokeRules?wsdl");
    }
}

