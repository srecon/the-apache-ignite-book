package com.blu.imdg.jdbc.model;

import java.time.LocalDate;

/**
 * Post entity for JDBC-based persistence store.
 */
public record Post(
    String id,
    String title,
    String description,
    LocalDate creationDate,
    String author
) {
}
