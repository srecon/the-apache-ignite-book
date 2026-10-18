package com.blu.imdg.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * Breed entity mapped to Ignite SQL queries.
 */
public record Breed(
    @QuerySqlField(index = true) Long id,
    @QuerySqlField(index = true) String name
) {
}
