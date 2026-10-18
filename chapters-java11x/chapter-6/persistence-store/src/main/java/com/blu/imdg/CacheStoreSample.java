package com.blu.imdg;

import com.blu.imdg.jdbc.PostgresDBStore;
import com.blu.imdg.jdbc.model.Post;
import com.blu.imdg.nosql.MongoDBStore;
import com.blu.imdg.nosql.model.MongoPost;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.apache.ignite.transactions.TransactionConcurrency.PESSIMISTIC;
import static org.apache.ignite.transactions.TransactionIsolation.REPEATABLE_READ;

public class CacheStoreSample {

    private static final Logger log = LoggerFactory.getLogger(CacheStoreSample.class);

    private static final String POST_CACHE_NAME = CacheStoreSample.class.getSimpleName() + "-post";
    private static final String POSTGRESQL = "postgresql";
    private static final String MONGODB = "mongodb";

    public static void main(String[] args) {
        if (args.length <= 0) {
            log.error("Usage! java -jar cache-store-runnable.jar postgresql|mongodb");
            System.exit(1);
        }

        switch (args[0].toLowerCase()) {
            case POSTGRESQL -> jdbcStoreExample();
            case MONGODB -> nosqlStore();
            default -> log.error("Unknown argument: {}. Use 'postgresql' or 'mongodb'.", args[0]);
        }
    }

    private static void jdbcStoreExample() {
        var cfg = new IgniteConfiguration();

        var configuration = new CacheConfiguration<String, Post>("dynamicCache");
        configuration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        configuration.setCacheStoreFactory(org.jcache.configuration.FactoryBuilder.factoryOf(PostgresDBStore.class));
        configuration.setReadThrough(true);
        configuration.setWriteThrough(true);
        configuration.setWriteBehindEnabled(true);

        log.info("Start. PersistenceStore example.");
        cfg.setCacheConfiguration(configuration);

        try (var ignite = Ignition.start(cfg)) {
            var count = 10;
            try (var igniteCache = ignite.getOrCreateCache(configuration)) {
                try (var tx = ignite.transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
                    for (var i = 1; i <= count; i++) {
                        igniteCache.put("_" + i, new Post("_" + i, "title-" + i, "description-" + i, LocalDate.now().plus(i, ChronoUnit.DAYS), "author-" + i));
                    }

                    tx.commit();

                    for (var i = 1; i < count; i += 2) {
                        igniteCache.remove("_" + i);
                        log.info("Clear every odd key: {}", i);
                    }

                    for (var i = 1L; i <= count; i++) {
                        log.info("Local peek at [key=_, {}], val={}", i, igniteCache.localPeek("_" + i));
                    }

                    for (var i = 1L; i <= count; i++) {
                        log.info("Got [key=_, {}], val={}", i, igniteCache.get("_" + i));
                    }

                    tx.commit();
                }
            }

            log.info("PersistenceStore example finished.");
        }
    }

    private static void nosqlStore() {
        var cfg = new IgniteConfiguration();

        var configuration = new CacheConfiguration<String, MongoPost>("mongoDynamicCache");
        configuration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        configuration.setCacheStoreFactory(org.jcache.configuration.FactoryBuilder.factoryOf(MongoDBStore.class));
        configuration.setReadThrough(true);
        configuration.setWriteThrough(true);
        configuration.setWriteBehindEnabled(true);

        log.info("Start. PersistenceStore example.");
        cfg.setCacheConfiguration(configuration);

        try (var ignite = Ignition.start(cfg)) {
            var count = 10;
            try (var igniteCache = ignite.getOrCreateCache(configuration)) {
                try (var tx = ignite.transactions().txStart(PESSIMISTIC, REPEATABLE_READ)) {
                    for (var i = 1; i <= count; i++) {
                        igniteCache.put("_" + i, new MongoPost("_" + i, "title-" + i, "description-" + i, "author-" + i));
                    }

                    tx.commit();
                }
            }

            log.info("PersistenceStore example finished.");
        }
    }
}
