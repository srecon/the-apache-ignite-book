package com.blu.imdg.nosql;

import com.blu.imdg.nosql.model.MongoPost;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for MongoPost entities.
 */
public interface PostRepository extends MongoRepository<MongoPost, String> {
}
