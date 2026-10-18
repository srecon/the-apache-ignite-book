FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/HelloIgnite-runnable.jar HelloIgnite-runnable.jar
CMD java --add-modules java.se \
    	 --add-opens java.base/jdk.internal.loader=ALL-UNNAMED \
    	 --add-opens java.base/jdk.internal.module=ALL-UNNAMED \
    	 --add-opens java.base/jdk.internal.ref=ALL-UNNAMED \
    	 --add-opens java.base/jdk.internal.perf=ALL-UNNAMED \
    	 --add-opens java.base/jdk.internal.reflect=ALL-UNNAMED \
    	 --add-opens java.base/jdk.internal.math=ALL-UNNAMED \
    	 --add-opens java.base/java.lang=ALL-UNNAMED \
    	 --add-opens java.base/java.nio=ALL-UNNAMED \
    	 --add-opens java.base/java.lang.module=ALL-UNNAMED \
    	 --add-opens java.base/java.lang.invoke=ALL-UNNAMED \
    	 --add-opens java.base/java.util=ALL-UNNAMED \
    	 --add-opens java.base/java.util.regex=ALL-UNNAMED \
    	 --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
    	 --add-opens java.base/jdk.internal.util.jar=ALL-UNNAMED \
    	 --add-opens java.xml/jdk.xml.internal=ALL-UNNAMED \
    	 --add-opens java.xml/javax.xml.catalog=ALL-UNNAMED \
    	 --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED \
    	 --add-opens jdk.management.jfr/jdk.management.jfr=ALL-UNNAMED \
    	 --add-exports java.base/jdk.internal.ref=ALL-UNNAMED \
    	 --add-exports java.naming/com.sun.jndi.ldap=ALL-UNNAMED \
    	 --add-opens java.management/sun.management=ALL-UNNAMED \
    	 -jar "HelloIgnite-runnable.jar"