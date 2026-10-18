package com.blu.imdg.example9;

import com.blu.imdg.common.bankData.BankDataGenerator;
import com.blu.imdg.common.bankData.model.AccountCacheData;
import com.blu.imdg.common.bankData.model.AccountCacheKey;
import com.blu.imdg.common.bankData.model.AccountData;
import com.blu.imdg.common.bankData.model.AccountKey;
import com.blu.imdg.example9.exception.AccountNotFoundException;
import com.blu.imdg.example9.exception.LogServiceException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.locks.Lock;

/**
 * Bank service implementation that validates daily transaction limits.
 */
public class BankServiceImpl implements BankService, Service {
    private static final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    @ServiceResource(serviceName = LogService.NAME)
    private LogService logService;

    @IgniteInstanceResource
    private Ignite ignite;

    IgniteCache<AccountCacheKey, AccountCacheData> accountCache;

    @Override
    public boolean validateOperation(String account, BigDecimal sum) throws AccountNotFoundException, LogServiceException {
        var key = new AccountKey(account);
        var lock = accountCache.lock(key);
        try {
            lock.lock();
            var accountData = (AccountData) accountCache.get(key);
            if (accountData == null) {
                log.warn("Account not found: {}", account);
                throw new AccountNotFoundException(account);
            }

            //clean today operations
            if (!accountData.getToday().equals(LocalDate.now())) {
                accountData.setTodayOperationSum(BigDecimal.ZERO);
                accountData.setToday(LocalDate.now());
                accountCache.put(key, accountData);
            }

            var newOperationSum = accountData.getTodayOperationSum().add(sum);
            if (newOperationSum.compareTo(accountData.getDailyLimit()) > 0) {
                logService.logOperation(account, false);
                log.info("Operation rejected for account {}: sum {} exceeds daily limit {}", account, sum, accountData.getDailyLimit());
                return false;
            } else {
                accountData.setTodayOperationSum(newOperationSum);
                accountCache.put(key, accountData);
                logService.logOperation(account, true);
                log.info("Operation approved for account {}: new daily total={}", account, newOperationSum);
                return true;
            }

        } finally {
            lock.unlock();
        }
    }

    @Override
    public void cancel(ServiceContext serviceContext) {
        log.info("Cancelling BankService");
    }

    @Override
    public void init(ServiceContext serviceContext) throws Exception {
        accountCache = ignite.getOrCreateCache(BankDataGenerator.ACCOUNT_CACHE);
        log.info("Initialized BankService with cache: {}", BankDataGenerator.ACCOUNT_CACHE);
    }

    @Override
    public void execute(ServiceContext serviceContext) throws Exception {

    }
}
