package com.blu.imdg.example9;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.bankData.BankDataGenerator;
import com.blu.imdg.common.bankData.model.AccountCacheData;
import com.blu.imdg.common.bankData.model.AccountCacheKey;
import com.blu.imdg.example9.exception.AccountNotFoundException;
import com.blu.imdg.example9.exception.LogServiceException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Deploys the bank and log services on an Ignite node.
 */
public class DeployService {
    private static final Logger log = LoggerFactory.getLogger(DeployService.class);

    public static void main(String[] args) throws AccountNotFoundException, LogServiceException {

        try (var ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            var cache = BankDataGenerator.createBankCache(ignite);

            var services = ignite.services().withAsync();

            services.deployNodeSingleton(LogService.NAME, new LogServiceImpl());
            services.future().get();
            log.info("Log Service deployed!");

            services.deployNodeSingleton(BankService.NAME, new BankServiceImpl());
            services.future().get();
            log.info("Bank Service deployed!");

        }
    }
}
