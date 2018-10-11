package com.blu.imdg.exampleOgm;

import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.hibernate.ogm.datastore.ignite.IgniteConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

/**
 * Created by mikl on 19.11.16.
 */
public class ConfigurationMaker implements IgniteConfigurationBuilder {
    @Override
    public IgniteConfiguration build() {
        IgniteConfiguration config = new IgniteConfiguration();
        config.setPeerClassLoadingEnabled(true);
        config.setClientMode(false);
        TcpDiscoverySpi discoSpi = new TcpDiscoverySpi();
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ArrayList<String> addrs = new ArrayList<>();
        addrs.add("127.0.0.1:47500..47509");
        ipFinder.setAddresses(addrs);
        discoSpi.setIpFinder(ipFinder);
        config.setDiscoverySpi(discoSpi);


        CacheConfiguration accountCacheCfg = new CacheConfiguration()
                .setName("BREED")
                .setAtomicityMode(TRANSACTIONAL)
                .setIndexedTypes(
                        String.class, Breed.class
                );

        config.setCacheConfiguration(accountCacheCfg);
        return config;
    }
}
