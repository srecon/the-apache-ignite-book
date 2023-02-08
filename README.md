# The Apache Ignite Book

<a href="http://leanpub.com/ignitebook"><img src="https://github.com/srecon/the-apache-ignite-book/blob/master/3D_mini.png" alt="he Apache Ignite Book" height="256px" align="right"></a>

This is the code repository (code samples, scripts and more in-depth examples) for [The Apache Ignite Book](https://leanpub.com/ignitebook).

## Naming conventions

Each chapter in the book has a corresponding folder within the repository. Each folder contains a set of files or folders related to each section of the chapter. For an example, the listing of the _memoization_ section is placed in the folder _chapters/chapter-5/memoization_.

## What is this book about?

Apache Ignite is one of the most widely used open source memory-centric distributed, caching, and processing platform. This allows the users to use the platform as an in- memory computing framework or a full functional persistence data stores with SQL and ACID transaction support. On the other hand, Apache Ignite can be used for accelerating existing Relational and NoSQL databases, processing events & streaming data or developing Microservices in fault-tolerant fashion.

This book addressed anyone interested in learning in-memory computing and distributed database. This book intends to provide someone with little to no experience of Apache Ignite with an opportunity to learn how to use this platform effectively from scratch taking a practical hands-on approach to learning.

This book covers the following exciting features:

* Apache Ignite architecture in depth such as data distributing technics (DHT), Rendezvous hashing, durable memory architecture, various cluster topologies, Ignite native persistence, Baseline topology and much more.
* Apache Ignite proven use cases as a memory-centric distributed database, caching and computing platforms.
* Getting started with Apache Ignite by using different tools and technics.
* Caching strategies by examples and how to use Apache Ignite for improving application performance including Hibernate L2 cache, MyBatis, memoization and web session cluster.
* Using Spring Data and JPA (Hibernate OGM) with Apache Ignite for developing high-performance web applications.
* Ignite query (SQL, API, Text and Scan queries) capabilities in depth.
* Using Spark RDD and Data frames for improving performance on processing fast data.
* Developing and executing distributed computations in a parallel fashion to gain high performance, low latency, and linear scalability. 
* Developing distributed Microservices in fault-tolerant fashion.
* Processing events & streaming data for IoT projects, integrate Apache Ignite with other frameworks like Kafka, Storm, Camel, etc.
* Configuring, management and monitoring Ignite cluster with built-in and 3rd party tools such.

If you feel this book is for you, get your [copy](https://leanpub.com/ignitebook) today!

_If you are not sure if this book is for you, I suggest you read the sample chapter of the book. The sample chapter is available in different formats including [HTML](https://leanpub.com/ignitebook/read_sample). Anyway, I encourage you to try it out, and if you don't like the book, you can always ask a 100% refund within 45 days._

## build and install

Note that, updated examples with Apache Ignite version 2.14.x are located on **chapters-java11x** folder. 

Folder **chapters** supports older Ignite version like 2.6.0.

Run the following command from the **chapters-java11x** or **chapters** directory

```
mvn clean install
```


We recommend a workstation with the following configurations for working with the repository:

| № | Name         | Value                                                        |
|---|--------------|--------------------------------------------------------------|
| 1 | JDK          | Oracle JDK 11.x and above.              |
| 2 | OS           | Linux, MacOS (10.8.3 and above), Windows Vista SP2 and above |
| 3 | Network      | No restriction                                               |
| 4 | RAM          | Minimum 4GB of RAM                                           |
| 5 | CPU          | Minimum 2 core                                               |
| 5 | IDE          | Eclipse, IntelliJ Idea, NetBeans or JDeveloper               |
| 6 | Apache Maven | Version 3.6.3 or above                                       |

## Conventions

The code will look like the following:
```
public class MySuperExtractor implements StreamSingleTupleExtractor<SinkRecord, String, String> {

  @Override public Map.Entry<String, String> extract(SinkRecord msg) {
      String[] parts = ((String)msg.value()).split("_");
      return new AbstractMap.SimpleEntry<String, String>(parts[1], parts[2]+":"+parts[3]);
  }
}
```
Any command-line input or output is written as follows:
```
[2018-12-30 15:39:04,479] INFO Kafka version : 2.0.0 (org.apache.kafka.common.utils.AppInfoParser)
[2018-12-30 15:39:04,479] INFO Kafka commitId : 3402a8361b734732 (org.apache.kafka.common.utils.AppInfoParser)
[2018-12-30 15:39:04,480] INFO [KafkaServer id=0] started (kafka.server.KafkaServer)
```
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
