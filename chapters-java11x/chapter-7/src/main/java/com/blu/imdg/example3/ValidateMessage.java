package com.blu.imdg.example3;

/**
 * Immutable message for XSD/JS validation, represented as a Java 21 record.
 */
public record ValidateMessage(
        String id,
        String msg,
        byte[] xsd,
        String js
) {}
