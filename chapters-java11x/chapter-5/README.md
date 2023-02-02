## Chapter 5 examples:

- Hibernate l2 cache
- memoization
- MyBatis caching
- Web session clustering

Note that. Ignite extension project moved to another github repositories and the artifactics are available from the following URL https://source.sakaiproject.org/maven2/

### To run Hibernate example:
1) Install PostgreSQL version 14 or above.
2) Use default configuration connection parameters or edit the /resources/jdbc.properties file corresponding to your configuration
3) execute the DDL and DML scripts from the folder /scripts
4) build and run the Webservice as follows: java --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED -jar ./target/hibernate-1.0-SNAPSHOT-spring-boot.jar
5) Use google chrome Wizdler plugin to invoke the services
NOTE that, Hibernate Query cache is not compatible with these Ignite version and excluded from these code base.

### To run Mybatis example:
1) Install PostgreSQL version 14 or above.
2) Use default configuration connection parameters or edit the /resources/jdbc.properties file corresponding to your configuration
3) execute the DDL and DML scripts from the folder /scripts
4) build and run the Webservice as follows: java --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED -jar ./target/mybatis-1.0-SNAPSHOT.one-jar.jar
5) Use google chrome Wizdler plugin to invoke the service getEmployee

