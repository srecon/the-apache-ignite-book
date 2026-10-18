package com.blu.imdg.common.bankData.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.time.LocalDate;

/**
 * Transaction key for Ignite SQL queries with affinity mapping.
 */
public class TransactionKey implements AccountCacheKey {

    @QuerySqlField(index = true)
    @AffinityKeyMapped
    private final String account;

    @QuerySqlField
    private final LocalDate transactionDate;

    private final String transactionId;

    public TransactionKey() {
        this.account = null;
        this.transactionDate = null;
        this.transactionId = null;
    }

    public TransactionKey(String account, LocalDate transactionDate, String transactionId) {
        this.account = account;
        this.transactionDate = transactionDate;
        this.transactionId = transactionId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        // Note: modifying a key after insertion is not recommended in Ignite.
        this.account = account;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "TransactionKey{" +
                "account='" + account + '\'' +
                ", transactionDate=" + transactionDate +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
