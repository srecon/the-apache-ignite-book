package com.blu.imdg;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.ignite.transactions.TransactionConcurrency.PESSIMISTIC;
import static org.apache.ignite.transactions.TransactionIsolation.REPEATABLE_READ;

public class SimpleTransactions {

    private static final Logger log = LoggerFactory.getLogger(SimpleTransactions.class);

    private static final String CACHE_NAME = SimpleTransactions.class.getSimpleName();

    public static void main(String[] args) {
        try (var ignite = Ignition.start("example-ignite.xml")) {
            log.info("|Transaction example started.|");

            var cfg = new CacheConfiguration<Integer, BankAccount>(CACHE_NAME);
            cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

            var ba1 = new BankAccount(100);
            var ba2 = new BankAccount(900);
            var ba3 = new BankAccount(400);

            try (var cache = ignite.getOrCreateCache(cfg)) {
                // Initialize accounts.
                cache.put(1, ba1);
                cache.put(2, ba2);
                cache.put(3, ba3);

                log.info("|--Bank accounts initialized: --|");
                log.info("|--Account #1: {}--|", cache.get(1));
                log.info("|--Account #2: {}--|", cache.get(2));
                log.info("|--Account #3: {}--|", cache.get(3));

                // Make transactional operations with bank accounts.
                deposit(cache, 1, 100);
                deposit(cache, 2, 200);

                log.info("|--Bank accounts after deposit: --|");
                log.info("|--Account #1: {}--|", cache.get(1));
                log.info("|--Account #2: {}--|", cache.get(2));

                withdraw(cache, 3, 10);
                withdraw(cache, 2, 500);
                withdraw(cache, 1, 100);

                log.info("|--Bank accounts after withdraw: --|");
                log.info("|--Account #3: {}--|", cache.get(3));
                log.info("|--Account #2: {}--|", cache.get(2));
                log.info("|--Account #1: {}--|", cache.get(1));

                log.info("|--Transaction example finished.--|");
            } finally {
                // Distributed cache could be removed from cluster only by destroyCache() call.
                ignite.destroyCache(CACHE_NAME);
            }
        }
    }

    /**
     * Make deposit into specified account number.
     */
    private static void deposit(IgniteCache<Integer, BankAccount> cache, int accountNumber, double amount) {
        try (var tx = Ignition.ignite().transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
            var bankAccount = cache.get(accountNumber);
            bankAccount.deposit(amount);
            cache.put(accountNumber, bankAccount);
            tx.commit();
        }

        log.info("");
        log.info("|--Deposit amount: ${}--|", amount);
    }

    /**
     * Make withdraw from specified account number.
     */
    private static void withdraw(IgniteCache<Integer, BankAccount> cache, int accountNumber, double amount) {
        var tx = Ignition.ignite().transactions().txStart(PESSIMISTIC, REPEATABLE_READ);
        try {
            var bankAccount = cache.get(accountNumber);
            bankAccount.withdraw(amount);
            cache.put(accountNumber, bankAccount);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            log.error("Withdraw failed for account {}: {}", accountNumber, e.getMessage());
        }

        log.info("");
        log.info("|--Withdraw amount: ${}--|", amount);
    }

    /**
     * BankAccount represents a bank account with deposit and withdraw operations.
     */
    private static class BankAccount {
        private static final AtomicInteger idSerial = new AtomicInteger(0);

        private final int accountNumber;
        private double accountBalance;

        public BankAccount() {
            this.accountNumber = idSerial.incrementAndGet();
            this.accountBalance = 0;
        }

        public BankAccount(double accountBalance) {
            this.accountNumber = idSerial.incrementAndGet();
            this.accountBalance = accountBalance;
        }

        public BankAccount(int accountNumber, double accountBalance) {
            this.accountNumber = accountNumber;
            this.accountBalance = accountBalance;
        }

        public boolean withdraw(double amount) {
            if (amount > accountBalance) {
                return false;
            }
            accountBalance -= amount;
            return true;
        }

        public boolean deposit(double amount) {
            if (amount < 0.0) {
                return false;
            }
            accountBalance += amount;
            return true;
        }

        @Override
        public String toString() {
            return "BankAccount{accountNumber=" + accountNumber + ", accountBalance=" + accountBalance + '}';
        }
    }
}
