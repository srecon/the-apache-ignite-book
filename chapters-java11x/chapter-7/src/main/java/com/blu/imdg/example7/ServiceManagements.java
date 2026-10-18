package com.blu.imdg.example7;

import com.blu.imdg.common.CommonConstants;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.services.ServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Lists all deployed Ignite services for monitoring purposes.
 */
public class ServiceManagements {
    private static final Logger log = LoggerFactory.getLogger(ServiceManagements.class);

    public static void main(String[] args) throws IOException{
        try (var ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            for (var serviceDescriptor : ignite.services().serviceDescriptors()) {
                log.info("Service Name: {}", serviceDescriptor.name());
                log.info("MaxPerNode count: {}", serviceDescriptor.maxPerNodeCount());
                log.info("Total count: {}", serviceDescriptor.totalCount());
                log.info("Service class Name: {}", serviceDescriptor.serviceClass());
                log.info("Origin Node ID: {}", serviceDescriptor.originNodeId());

            }

        }
    }
}
