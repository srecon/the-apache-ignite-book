package com.blu.imdg.wsession.bean;

import java.io.Serializable;

public class Person implements Serializable{
    private String firstName;
    private int    age;

    public Person(String firstName) {
        this.firstName = firstName;
    }

    public Person() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
