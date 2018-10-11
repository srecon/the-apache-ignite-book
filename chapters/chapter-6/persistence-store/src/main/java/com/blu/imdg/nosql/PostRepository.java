package com.blu.imdg.nosql;

import com.blu.imdg.nosql.model.MongoPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;


public interface PostRepository extends MongoRepository<MongoPost, String>{

}
