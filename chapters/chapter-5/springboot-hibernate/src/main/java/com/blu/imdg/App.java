package com.blu.imdg.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        System.out.println("Starting Spring boot app!!");
        SpringApplication.run(App.class, args);
    }
}
