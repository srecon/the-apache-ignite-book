package com.blu.imdg;

import com.blu.imdg.dto.Employee;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        //Start Ignite before initiliazing Spring context
        Ignite ignite = Ignition.start("ignite-l2-config.xml");

        System.out.println("Starting Spring boot app!!");
        SpringApplication.run(App.class, args);
    }
    // Ignite starter
//    @Bean
//    public Ignite igniteInstance(){
//        IgniteConfiguration cfg = new IgniteConfiguration();
//
//        // Setting some custom name for the node.
//        cfg.setIgniteInstanceName("springDataNode");
//
//        // Enabling peer-class loading feature.
//        cfg.setPeerClassLoadingEnabled(true);
//
//        // Defining and creating a new cache to be used by Ignite Spring Data
//        // repository.
//        CacheConfiguration ccfgEmployee = new CacheConfiguration("EmployeeCache");
//        ccfgEmployee.setCacheMode(CacheMode.PARTITIONED);
//        ccfgEmployee.setAtomicityMode(CacheAtomicityMode.ATOMIC);
//
//
//        // Setting SQL schema for the cache.
//        ccfgEmployee.setIndexedTypes(Long.class, Employee.class);
//
//
//        cfg.setCacheConfiguration(new CacheConfiguration[]{ccfgEmployee});
//
//
//        return Ignition.start(cfg);
//
//    }
}
