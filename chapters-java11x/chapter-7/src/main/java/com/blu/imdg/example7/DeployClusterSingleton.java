package com.blu.imdg.example7;

import com.blu.imdg.common.CommonConstants;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Deploys the XSD validation service as a cluster singleton on Ignite.
 */
public class DeployClusterSingleton {
    private static final Logger log = LoggerFactory.getLogger(DeployClusterSingleton.class);
    private static final String VALIDATING_SERVICE = "validatingService-cluster-singleton";

    public static void main(String[] args) throws IOException {
        try (var ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            log.info("Deploying cluster singleton XSD validation service");
            ignite.services().deployClusterSingleton(VALIDATING_SERVICE, new XsdValidatingServiceImpl());

        }

    }
}
