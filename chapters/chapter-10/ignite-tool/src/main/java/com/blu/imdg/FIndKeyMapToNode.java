package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryField;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

import java.util.Arrays;

public class FIndKeyMapToNode {
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

        //cfg.setIgniteInstanceName("DataNode");
        cfg.setClientMode(true);
        // set the discovery§ spi to ignite configuration
        cfg.setDiscoverySpi(spi);
        // Start ignite
        Ignite ignite = Ignition.start(cfg);
        final Long key = 2000L;

        IgniteCache<Long, BinaryObject> cache = ignite.cache("EMPcache").withKeepBinary();
        BinaryObject binaryObject = cache.get(key);

        BinaryField enameField=  binaryObject.type().field("ename");
        String ename =  enameField.value(binaryObject);
        System.out.println("Ename:"+ ename);

        Affinity affinity = ignite.affinity("EMPcache");
        ClusterNode primary = affinity.mapKeyToNode(key);
        System.out.println("node:" + primary.id().toString());

        System.out.println("Partition ID:" + affinity.partition(key));

        ignite.close();

    }
}
