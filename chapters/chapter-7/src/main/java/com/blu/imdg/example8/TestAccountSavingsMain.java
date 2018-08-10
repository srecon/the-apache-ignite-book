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

import javax.cache.Cache;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */

//mvn exec:java -Dexec.mainClass=com.blu.imdg.example8.TestAccountSavingsMain
public class TestAccountSavingsMain {

    public static void main(String[] args) {

        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {
            IgniteCompute compute = ignite.compute();

            IgniteCache<AccountCacheKey, AccountCacheData> cache = BankDataGenerator.createBankCache(ignite);
            IgniteCache<String, CashBackDictionaryData> savingsCache = BankDataGenerator.initSavigsCache(ignite);
            System.out.println("savingsCache=" + savingsCache);

            SqlQuery<TransactionKey, TransactionData> sql = new SqlQuery(TransactionData.class,"select * from TransactionData where account = ?");

            BigDecimal result = compute.affinityCall(BankDataGenerator.ACCOUNT_CACHE, new AccountKey(BankDataGenerator.TEST_ACCOUNT), () -> {
                //download all transactions for this account
                List<Cache.Entry<TransactionKey, TransactionData>> data = cache.query(sql.setArgs(BankDataGenerator.TEST_ACCOUNT)).getAll();
                System.out.println("data="+data);;
                BigDecimal cashBack = new BigDecimal(0);
                for (Cache.Entry<TransactionKey, TransactionData> row : data) {
//                    String key = (String) row.get(0);
//                    System.out.println("key="+row);
                    TransactionData tr = (TransactionData) row.getValue();
                    IgniteCache<String, CashBackDictionaryData> savingsCacheLocal = ignite.cache("savingsDictionaryCache");
                    System.out.println("savingsCache=" + savingsCacheLocal);


                    CashBackDictionaryData cashBackDictionaryData = savingsCache.get(tr.getTransactionType());
                    cashBack = cashBack.add(tr.getSum().multiply(cashBackDictionaryData.getCashBackPercent()));
                }
                System.out.println("savings=" + cashBack);
                return cashBack;
            });

            System.out.println("CashBack=" + result);

        }
    }

}

//                    TransactionKey key = (TransactionKey) row.get(0);
//    String key = (String) row.get(0);

//                    TransactionData tr = (TransactionData) row.get(1);
//
//                    CashBackDictionaryData cashBackDictionaryData = savingsCache.get(tr.getTransactionType());
//                    cashBack = cashBack.add(tr.getSum().multiply(cashBackDictionaryData.getCashBackPercent()));
