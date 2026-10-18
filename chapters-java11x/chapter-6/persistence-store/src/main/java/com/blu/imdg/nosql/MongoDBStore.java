package com.blu.imdg.nosql;

import com.blu.imdg.nosql.model.MongoPost;
import org.apache.ignite.IgniteException;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lifecycle.LifecycleAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

public class MongoDBStore extends CacheStoreAdapter<String, MongoPost> implements LifecycleAware {

    private static final Logger log = LoggerFactory.getLogger(MongoDBStore.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public MongoPost load(String key) throws CacheLoaderException {
        log.info("Loading post with key: {}", key);
        return postRepository.findById(key).orElse(null);
    }

    @Override
    public void write(Cache.Entry<? extends String, ? extends MongoPost> entry) throws CacheWriterException {
        var post = entry.getValue();
        log.info("Writing post: {}", post);
        postRepository.save(post);
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        log.info("Deleting post with key: {}", key);
        postRepository.deleteById((String) key);
    }

    @Override
    public void start() throws IgniteException {
        var context = new ClassPathXmlApplicationContext("mongo-context.xml");
        postRepository = context.getBean(PostRepository.class);
        log.info("PostRepository initialized: {}", postRepository);
        mongoOperations = context.getBean(MongoOperations.class);
        if (!mongoOperations.collectionExists(MongoPost.class)) {
            mongoOperations.createCollection(MongoPost.class);
        }
    }

    @Override
    public void stop() throws IgniteException {
        // No-op
    }
}
