package com.blu.imdg.nosql;

import com.blu.imdg.nosql.model.MongoPost;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface PostRepository extends MongoRepository<MongoPost, String>{

}
