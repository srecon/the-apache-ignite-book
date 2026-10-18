package com.blu.imdg.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * Dog entity mapped to Ignite SQL queries.
 */
public record Dog(
    @QuerySqlField(index = true) Long id,
    @QuerySqlField(index = true) String name,
    @QuerySqlField(index = true) Long breedId,
    @QuerySqlField(index = true) java.time.LocalDate birthdate
) {
}
