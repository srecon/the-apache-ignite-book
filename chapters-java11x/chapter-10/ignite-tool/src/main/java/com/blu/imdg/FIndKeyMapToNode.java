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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;

public class FindKeyMapToNode {
    private static final Logger log = LogManager.getLogger(FindKeyMapToNode.class);

    public static void main(String[] args) {
        log.info("Hello Ignite");

        var spi = new TcpDiscoverySpi();
        var tcMp = new TcpDiscoveryMulticastIpFinder();
        tcMp.setAddresses(Arrays.asList("localhost"));
        spi.setIpFinder(tcMp);

        var cfg = new IgniteConfiguration();
        cfg.setClientMode(true);
        cfg.setDiscoverySpi(spi);

        var ignite = Ignition.start(cfg);
        final Long key = 2000L;

        try (var cache = ignite.cache("EMPcache").withKeepBinary()) {
            var binaryObject = cache.get(key);

            var enameField = binaryObject.type().field("ename");
            var ename = enameField.value(binaryObject);
            log.info("Ename: {}", ename);

            var affinity = ignite.affinity("EMPcache");
            var primary = affinity.mapKeyToNode(key);
            log.info("node: {}", primary.id());

            log.info("Partition ID: {}", affinity.partition(key));
        } finally {
            ignite.close();
        }
    }
}
