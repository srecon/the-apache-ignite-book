# The Apache Ignite Book

This is the code repository (code samples, scripts and more in-depth examples) for [The Apache Ignite Book](https://leanpub.com/ignitebook).

[![alt text](/mini-cover.png "book cover")](http://leanpub.com/ignitebook)

## build and install

Run the following command from the **chapters** directory

```
mvn clean install
```

The above command will build all the Maven modules except:

- /chapter-5/hibernate
- /chapter-5/Hibernate memoization
- /chapter-6/ignite-hibernate-ogm

The above modules required **ignite-hibernate 5.1** library which is not available in the central maven repository. So, for modules **/chapter-5/hibernate** and **/chapter-5/Hibernate memoization** you need to build them from the Apache Ignite source code and install in your local maven repository. Therefore, download the source code from the Apache Ignite site and follow the steps 1-9 from the chapter 2 section “Building from source code”. After completing step 9, you should have installed all the required artifacts in your local repository.

The **/chapter-6/ignite-hibernate-ogm** project required an Ignite dialect for Hibernate OGM. The Maven module uses **hibernate-ogm-ignite** version 5.4.0 snapshot which is also not available in the central Maven repository. So, you need to clone the project from the GitHub and build it yourself. The entire process of building the project from source code described in chapter 6.


We recommend a workstation with the following configurations for working with the repository:

| № | Name         | Value                                                        |
|---|--------------|--------------------------------------------------------------|
| 1 | JDK          | Oracle JDK 8 and above.              |
| 2 | OS           | Linux, MacOS (10.8.3 and above), Windows Vista SP2 and above |
| 3 | Network      | No restriction                                               |
| 4 | RAM          | Minimum 4GB of RAM                                           |
| 5 | CPU          | Minimum 2 core                                               |
| 5 | IDE          | Eclipse, IntelliJ Idea, NetBeans or JDeveloper               |
| 6 | Apache Maven | Version 3.3.1 or above                                       |


## This Github repository contains the following examples:
- Ignite example in Java
- Ignite example in Spring
- Ignite thinclient example
- Ignite isolated cluster example
- Ignite with Hibernate example
- Ignite with MyBatis example
- Memoization example in Ignite
- Ignite web session clustering example
- Ignite SQL examples
- Ignite QueryApI example
- Ignite text query example
- Ignite distributed SQL JOINs example
- Ignite Spring data example
- Ignite compute grid examples
- Microservices with Ignite examples
- Ignite camel itegration example
- Ignite flume integration example
- Ignite kafka integration example
- Ignite Storm itegration example
- Ignite Spark RDD example
- Ignite Spark Data frame example
- Ignite Zookeeper discovery example
- Ignite Baseline by examples
- Ignite monitoring by VisualVM/Grafana example
- and much more
