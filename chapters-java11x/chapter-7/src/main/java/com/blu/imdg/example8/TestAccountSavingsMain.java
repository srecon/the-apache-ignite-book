package com.blu.imdg.example8;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.bankData.BankDataGenerator;
import com.blu.imdg.common.bankData.model.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import java.math.BigDecimal;
import java.util.List;

/**
 * Demonstrates account savings with cashback calculation across Ignite caches.
 * <p>
 * mvn exec:java -Dexec.mainClass=com.blu.imdg.example8.TestAccountSavingsMain
 */
public class TestAccountSavingsMain {
    private static final Logger log = LoggerFactory.getLogger(TestAccountSavingsMain.class);

    public static void main(String[] args) {

        try (var ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {
            var compute = ignite.compute();

            var cache = BankDataGenerator.createBankCache(ignite);
            var savingsCache = BankDataGenerator.initSavigsCache(ignite);
            log.info("savingsCache={}", savingsCache);

            var sql = new SqlQuery<TransactionKey, TransactionData>(TransactionData.class, "select * from TransactionData where account = ?");

            BigDecimal result = compute.affinityCall(BankDataGenerator.ACCOUNT_CACHE, new AccountKey(BankDataGenerator.TEST_ACCOUNT), () -> {
                //download all transactions for this account
                var data = cache.query(sql.setArgs(BankDataGenerator.TEST_ACCOUNT)).getAll();
                log.info("data={}", data);

                BigDecimal cashBack = BigDecimal.ZERO;
                for (var row : data) {
                    var tr = (TransactionData) row.getValue();
                    var savingsCacheLocal = ignite.cache("savingsDictionaryCache");
                    log.debug("Checking savings cache: {}", savingsCacheLocal);

                    var cashBackDictionaryData = savingsCache.get(tr.getTransactionType());
                    cashBack = cashBack.add(tr.getSum().multiply(cashBackDictionaryData.getCashBackPercent()));
                }
                log.info("cashBack for account={}", cashBack);
                return cashBack;
            });

            log.info("CashBack={}", result);

        }
    }

}
