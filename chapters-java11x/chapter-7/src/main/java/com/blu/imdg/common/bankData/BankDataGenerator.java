package com.blu.imdg.common.bankData;

import com.blu.imdg.common.bankData.model.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;

/**
 * Generates and initializes bank test data in Ignite caches.
 */
public class BankDataGenerator {
    private static final Logger log = LoggerFactory.getLogger(BankDataGenerator.class);

    public static final String ACCOUNT_CACHE = "accountCache";
    public static final String SAVINGS_CACHE = "savingsDictionaryCache";
    public static final String TEST_ACCOUNT = "0000*1111";
    public static final String TEST_UNKNOWN_ACCOUNT = "0000*3333";

    public static IgniteCache<AccountCacheKey, AccountCacheData> createBankCache(Ignite ignite) {
        var accountCacheCfg = new CacheConfiguration()
                .setName(ACCOUNT_CACHE)
                .setAtomicityMode(TRANSACTIONAL)
                .setIndexedTypes(
                        AccountKey.class, AccountData.class,
                        TransactionKey.class, TransactionData.class,
                        String.class, CashBackDictionaryData.class
                );

        var result = ignite.getOrCreateCache(accountCacheCfg);
        result.removeAll();
        return initData(result);
    }

    public static IgniteCache<String, CashBackDictionaryData> initSavigsCache(Ignite ignite) {
        var savingsCacheCfg = new CacheConfiguration()
                .setName(SAVINGS_CACHE)
                .setIndexedTypes(
                        String.class, CashBackDictionaryData.class
                );

        var result = ignite.getOrCreateCache(savingsCacheCfg);
        result.removeAll();
        result.put("meal", new CashBackDictionaryData(BigDecimal.valueOf(0.01), "meal shopping"));
        result.put("entertainment", new CashBackDictionaryData(BigDecimal.valueOf(0.02), "entertainment"));
        log.info("Initialized savings dictionary cache with {} entries", 2);
        return result;
    }

    private static IgniteCache<AccountCacheKey, AccountCacheData> initData(IgniteCache<AccountCacheKey, AccountCacheData> result) {
        //init data
        result.put(new AccountKey(TEST_ACCOUNT), new AccountData(TEST_ACCOUNT, "John Doe", "standard", LocalDate.now(), BigDecimal.valueOf(100)));
        result.put(new AccountKey(TEST_ACCOUNT), new AccountData(TEST_ACCOUNT, "Mr. Smith", "premium", LocalDate.now(), BigDecimal.valueOf(200)));

        result.put(
                new TransactionKey(TEST_ACCOUNT, LocalDate.now(), UUID.randomUUID().toString()),
                new TransactionData(TEST_ACCOUNT, "1111*2222", BigDecimal.valueOf(100), "meal")
        );

        result.put(
                new TransactionKey(TEST_ACCOUNT, LocalDate.now(), UUID.randomUUID().toString()),
                new TransactionData(TEST_ACCOUNT, "3333*4444", BigDecimal.valueOf(100), "entertainment")
        );

        log.info("Initialized bank cache with test data for account {}", TEST_ACCOUNT);
        return result;
    }
}
