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

import java.math.BigDecimal;

/**
 * Tests the bank and log microservices deployment on Ignite.
 */
public class TestMicroServiceMain {
    private static final Logger log = LoggerFactory.getLogger(TestMicroServiceMain.class);

    public static void main(String[] args) throws AccountNotFoundException, LogServiceException {
        try (var ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            var cache = BankDataGenerator.createBankCache(ignite);

            var services = ignite.services().withAsync();

            services.deployNodeSingleton(LogService.NAME, new LogServiceImpl());
//            services.future().get();

            services.deployNodeSingleton(BankService.NAME, new BankServiceImpl());
//            services.future().get();

            var bankService = services.serviceProxy(BankService.NAME, BankService.class, /*not-sticky*/false);

            log.info("result={}", bankService.validateOperation(BankDataGenerator.TEST_ACCOUNT, new BigDecimal(50)));
            log.info("result1={}", bankService.validateOperation(BankDataGenerator.TEST_ACCOUNT, new BigDecimal(40)));
            log.info("result2={}", bankService.validateOperation(BankDataGenerator.TEST_ACCOUNT, new BigDecimal(180)));

            services.cancel(BankService.NAME);
        }
    }
}
