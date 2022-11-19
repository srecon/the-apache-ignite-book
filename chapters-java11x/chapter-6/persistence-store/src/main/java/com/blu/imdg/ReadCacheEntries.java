package com.blu.imdg;

import com.blu.imdg.nosql.MongoDBStore;
import com.blu.imdg.nosql.model.MongoPost;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.cache.configuration.FactoryBuilder;


public class ReadCacheEntries {
    private static Logger LOGGER = LoggerFactory.getLogger(CacheStoreSample.class);

    public static void main(String[] args) throws Exception {

        IgniteConfiguration cfg = new IgniteConfiguration();

        CacheConfiguration configuration = new CacheConfiguration();
        configuration.setName("mongoDynamicCache");

        configuration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

        configuration.setCacheStoreFactory(FactoryBuilder.factoryOf(MongoDBStore.class));
        configuration.setReadThrough(true);
        configuration.setWriteThrough(true);

        configuration.setWriteBehindEnabled(true);

        cfg.setCacheConfiguration(configuration);

        try (Ignite ignite = Ignition.start(cfg)) {

            try (IgniteCache<String, MongoPost> igniteCache = ignite.getOrCreateCache(configuration)) {
                final String key = "_9";
                log(igniteCache.get(key).toString());
            }

            Thread.sleep(Integer.MAX_VALUE);
        }
    }

    /**
     * Prints message to logger.
     *
     * @param msg String.
     */
    private static void log(String msg) {
        LOGGER.info("\t" + msg);
    }
}
