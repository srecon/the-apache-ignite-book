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

public class ReadCacheEntries {

    private static final Logger log = LoggerFactory.getLogger(ReadCacheEntries.class);

    public static void main(String[] args) throws Exception {
        var cfg = new IgniteConfiguration();

        var configuration = new CacheConfiguration<String, MongoPost>("mongoDynamicCache");
        configuration.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        configuration.setCacheStoreFactory(org.jcache.configuration.FactoryBuilder.factoryOf(MongoDBStore.class));
        configuration.setReadThrough(true);
        configuration.setWriteThrough(true);
        configuration.setWriteBehindEnabled(true);

        cfg.setCacheConfiguration(configuration);

        try (var ignite = Ignition.start(cfg)) {
            try (var igniteCache = ignite.getOrCreateCache(configuration)) {
                var key = "_9";
                log.info("{}", igniteCache.get(key));
            }
        }
    }
}
