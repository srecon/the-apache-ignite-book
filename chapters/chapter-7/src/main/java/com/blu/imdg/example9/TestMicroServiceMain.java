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

import java.math.BigDecimal;

/**
 *
 * теперь нельзя убедиться что future отработало
 */
//mvn exec:java -Dexec.mainClass=com.blu.imdg.example9.TestMicroServiceMain
public class TestMicroServiceMain {

    public static void main(String[] args) throws AccountNotFoundException, LogServiceException, InterruptedException {
        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            IgniteCache<AccountCacheKey, AccountCacheData> cache = BankDataGenerator.createBankCache(ignite);

            IgniteServices services = ignite.services().withAsync();

            services.deployNodeSingleton(LogService.NAME, new LogServiceImpl());
//            services.future().get();

            services.deployNodeSingleton(BankService.NAME, new BankServiceImpl());
            Thread.sleep(5000);
//            services.future().get();

            BankService bankService = services.serviceProxy(BankService.NAME, BankService.class, /*not-sticky*/false);

            System.out.println("result=" + bankService.validateOperation(BankDataGenerator.TEST_ACCOUNT, new BigDecimal(50)));
            System.out.println("result1=" + bankService.validateOperation(BankDataGenerator.TEST_ACCOUNT, new BigDecimal(40)));
            System.out.println("result2=" + bankService.validateOperation(BankDataGenerator.TEST_ACCOUNT, new BigDecimal(180)));

            services.cancel(BankService.NAME);
        }
    }
}
