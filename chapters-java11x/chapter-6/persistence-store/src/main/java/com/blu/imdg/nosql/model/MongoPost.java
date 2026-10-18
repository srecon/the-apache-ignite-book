package com.blu.imdg.nosql.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoPost entity for MongoDB persistence store.
 */
@Document(collection = "mongopost")
public record MongoPost(
    @Id String id,
    String title,
    String description,
    String author
) {
}
