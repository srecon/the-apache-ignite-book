package com.blu.imdg;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;


import java.util.Arrays;
/**
 * User: bsha
 */
public class HelloIgnite {
    public static void main(String[] args) {
        System.out.println("Hello Ignite");
        // create a new instance of TCP Discovery SPI
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        // create a new instance of tcp discovery multicast ip finder
        TcpDiscoveryMulticastIpFinder tcMp = new TcpDiscoveryMulticastIpFinder();
        tcMp.setAddresses(Arrays.asList("localhost")); // change your IP address here
        // set the multi cast ip finder for spi
        spi.setIpFinder(tcMp);
        // create new ignite configuration
        IgniteConfiguration cfg = new IgniteConfiguration();
        //cfg.setIgniteInstanceName("IgniteDataNode");
        cfg.setClientMode(false);
        // set the discovery§ spi to ignite configuration
        cfg.setDiscoverySpi(spi);
        // Start ignite
        Ignite ignite = Ignition.start(cfg);
        // get or create cache
        IgniteCache<Integer, String> cache = ignite.getOrCreateCache("testCache");
        // put some cache elements
        for(int i = 1; i <= 100; i++){
            cache.put(i, "Hello Worlds: "+Integer.toString(i));
        }
        // get them from the cache and write to the console
        for(int i =1; i<= 100; i++){
            System.out.println("Cache get:"+ cache.get(i));
        }
        ignite.close();

    }
}
