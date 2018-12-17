package com.blu.imdg.exampleOgm;


import org.apache.ignite.configuration.IgniteConfiguration;

import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import org.hibernate.ogm.datastore.ignite.IgniteConfigurationBuilder;

import java.util.Arrays;


public class ConfigurationMaker implements IgniteConfigurationBuilder {
    @Override
    public IgniteConfiguration build() {

        TcpDiscoverySpi spi = new TcpDiscoverySpi();

        TcpDiscoveryVmIpFinder tcVm = new TcpDiscoveryVmIpFinder();
        // change your IP address here
        tcVm.setAddresses(Arrays.asList("127.0.0.1:47500..47509"));
        // set the static ip finder for spi
        spi.setIpFinder(tcVm);
        // create new ignite configuration
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(false);
        // set the discovery spi to ignite configuration
        cfg.setDiscoverySpi(spi);

        cfg.setIgniteInstanceName("ignite-ogm");

        return cfg;

    }
}
