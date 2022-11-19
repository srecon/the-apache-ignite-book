package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryField;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Arrays;

public class Benchmark {
    public static void main(String[] args) {
        System.out.println("Hello Ignite");
        // create a new instance of TCP Discovery SPI
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        // create a new instance of tcp discovery multicast ip finder
        TcpDiscoveryMulticastIpFinder tcMp = new TcpDiscoveryMulticastIpFinder();
        tcMp.setAddresses(Arrays.asList("127.0.0.1")); // change your IP address here
        // set the multi cast ip finder for spi
        spi.setIpFinder(tcMp);
        // create new ignite configuration
        IgniteConfiguration cfg = new IgniteConfiguration();
        //cfg.setIgniteInstanceName("Benchmark Node");
        cfg.setClientMode(true);
        cfg.setPeerClassLoadingEnabled(true);

        // set the discovery§ spi to ignite configuration
        cfg.setDiscoverySpi(spi);
        // Start ignite
        Ignite ignite = Ignition.start(cfg);
        // get or create cache
        IgniteCache<Long, BinaryObject> cache = ignite.cache("SQL_PUBLIC_EMP").withKeepBinary();
        BinaryObject binaryObject = cache.get(900l);

        BinaryField enameField=  binaryObject.type().field("ename");
        String ename =  enameField.value(binaryObject);
        System.out.println("Ename:"+ ename);

        //System.out.println("class name:" + binaryObject.getClass().getName());

        ignite.close();

    }
}
