package com.blu.imdg;

import com.blu.dto.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
/**
 * Running the example with Java 11 or later
 * java --add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED --add-opens=java.base/jdk.internal.misc=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED --add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED --add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED --add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED -jar ./target/HelloIgniteSpring-runnable.jar
 * */
public class HelloIgniteSpring {
    public static void main(String[] args) {
        // Start Ignite cluster
        Ignite ignite = Ignition.start("default-config.xml");

        // get or create cache
        IgniteCache<Integer, Person> cache =  ignite.getOrCreateCache("testCache");
        Person p1 = new Person(37, "Shamim");
        Person p2 = new Person(2, "Mishel");
        Person p3 = new Person(55, "scott");
        Person p4 = new Person(5, "Tiger");

        cache.put(1, p1);
        cache.put(2, p2);
        cache.put(3, p3);
        cache.put(4, p4);

        System.out.println("Enter crtl-x to quite the application!!!");

    }
}
