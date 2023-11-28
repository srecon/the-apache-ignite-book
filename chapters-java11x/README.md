# The Apache Ignite Book

<a href="http://leanpub.com/ignitebook"><img src="https://github.com/srecon/the-apache-ignite-book/blob/master/3D_mini.png" alt="he Apache Ignite Book" height="256px" align="right"></a>

## build and install

Examples contained in this folder uses Apache Ignite version **2.14.x**. To compile and build the entire project, please run the following command from the **chapters-java11x** directory

```
mvn clean install
```
We recommend a workstation with the following configurations for working with the repository:

| № | Name         | Value                                                |
|---|--------------|------------------------------------------------------|
| 1 | JDK          | Oracle JDK 11 or 17.                                 |
| 2 | OS           | Linux, MacOS (11.6.1 and above), Windows 7 and above |
| 3 | Network      | No restriction                                       |
| 4 | RAM          | Minimum 4GB of RAM                                   |
| 5 | CPU          | Minimum 2 core                                       |
| 5 | IDE          | Eclipse, IntelliJ Idea, NetBeans or JDeveloper       |
| 6 | Apache Maven | Version 3.6.3 or above                               |

> Note that, you have to use the following Java options to run examples on JVM 17 or later. Each chapter also contained `README.md` file with short instructions.

```
--add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED
```
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
- Ignite camel integration example
- Ignite flume integration example
- Ignite kafka integration example
- Ignite Storm integration example
- Ignite Spark RDD example
- Ignite Spark Data frame example
- Ignite Zookeeper discovery example
- Ignite Baseline by examples
- Ignite monitoring by VisualVM/Grafana example
- and much more
