package com.blu.imdg.exampleOgm;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryBasicNameMapper;
import org.apache.ignite.configuration.BinaryConfiguration;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.TransactionConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.hibernate.ogm.datastore.ignite.IgniteConfigurationBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

/**
 * Created by mikl on 19.11.16.
 */
public class ConfigurationMaker implements IgniteConfigurationBuilder {
    @Override
    public IgniteConfiguration build() {
//        Ignite ignite = Ignition.start("default-config.xml");
//        IgniteConfiguration config = ignite.configuration();
//        System.out.println("Ignite home:"+config.getIgniteHome());
//
//        return ignite.configuration();
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        // create a new instance of tcp discovery multicast ip finder
        TcpDiscoveryMulticastIpFinder tcMp = new TcpDiscoveryMulticastIpFinder();
        tcMp.setAddresses(Arrays.asList("127.0.0.1:47500..47509")); // change your IP address here
        //tcMp.setLocalAddress("localhost");
        //tcMp.setMulticastPort(47500);
        //tcMp.setMulticastPort(47501);
        // set the multi cast ip finder for spi
        spi.setIpFinder(tcMp);
        // create new ignite configuration
        IgniteConfiguration cfg = new IgniteConfiguration();
        //cfg.setIgniteInstanceName("IgniteDataNode");
        cfg.setClientMode(false);
        // set the discovery§ spi to ignite configuration
        //cfg.setDiscoverySpi(spi);
        cfg.setIgniteInstanceName("ignite-ogm");

        return cfg;

        //IgniteConfiguration config = new IgniteConfiguration();
//        config.setPeerClassLoadingEnabled(true);
//        config.setClientMode(false);
//        TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
//        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
//        ArrayList<String> addrs = new ArrayList<>();
//        addrs.add("127.0.0.1:47500..47509");
//        ipFinder.setAddresses(addrs);
//        discoSpi.setIpFinder(ipFinder);
//        config.setDiscoverySpi(discoSpi);
//
//
//        CacheConfiguration accountCacheCfg = new CacheConfiguration()
//                .setName("BREED")
//                .setAtomicityMode(TRANSACTIONAL)
//                .setIndexedTypes(
//                        String.class, Breed.class
//                );
//
//        config.setCacheConfiguration(accountCacheCfg);

        //return config;
    }
}
