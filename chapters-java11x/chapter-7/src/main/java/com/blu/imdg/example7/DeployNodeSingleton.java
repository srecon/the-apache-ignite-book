package com.blu.imdg.example7;

import com.blu.imdg.common.CommonConstants;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Deploys the XSD validation service as a node singleton on Ignite.
 */
public class DeployNodeSingleton {
    private static final Logger log = LoggerFactory.getLogger(DeployNodeSingleton.class);
    private static final String VALIDATING_SERVICE = "validatingService";

    public static void main(String[] args) throws IOException{
        try (var ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            log.info("Deploying node singleton XSD validation service");
            ignite.services().deployNodeSingleton(VALIDATING_SERVICE, new XsdValidatingServiceImpl());

        }

    }
}
